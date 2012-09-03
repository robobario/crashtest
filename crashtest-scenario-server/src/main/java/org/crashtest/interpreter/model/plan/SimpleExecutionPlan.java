package org.crashtest.interpreter.model.plan;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.crashtest.common.model.ParameterDescription;
import org.crashtest.interpreter.ExecutionPlan;
import org.crashtest.interpreter.Scope;
import org.crashtest.interpreter.model.*;
import org.crashtest.interpreter.model.expressions.Identifier;
import org.crashtest.interpreter.model.expressions.Literal;
import org.crashtest.interpreter.model.plan.descriptions.MethodInvocationStep;
import org.crashtest.interpreter.model.plan.descriptions.RemoteMethodInvocationStep;
import org.crashtest.interpreter.model.plan.descriptions.ScriptInvocationStep;
import org.crashtest.interpreter.model.statements.MethodInvocation;
import org.crashtest.interpreter.model.statements.RemoteInvocation;
import org.crashtest.service.ProgressReportProvider;
import org.crashtest.service.RemoteInvokerService;
import org.crashtest.service.model.RemoteInvocationDescription;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class SimpleExecutionPlan implements ExecutionPlan{
    ExecutionTree tree;

    private SimpleExecutionPlan(ExecutionTree tree) {
        this.tree = tree;
    }

    @Override
    public Execution execute(final RemoteInvokerService service, ExecutorService executorService) {
        final ProgressAwareExecutionTreeWalker newProgressAwareTreeWalker = tree.getNewProgressAwareTreeWalker();
        return new Execution(executorService.submit(new Runnable() {
            @Override
            public void run() {
                newProgressAwareTreeWalker.depthFirst(new InvocationStepVisitor() {
                    @Override
                    public void visit(MethodInvocationStep step) {
                    }

                    @Override
                    public void visit(RemoteMethodInvocationStep step) {
                        service.invoke(RemoteInvocationDescription.named(step.getName()).withParameters(step.getParameters()).build());
                    }

                    @Override
                    public void visit(ScriptInvocationStep step) {
                    }
                });
            }
        }),newProgressAwareTreeWalker);
    }

    public static SimpleExecutionPlan planFor(Script script, Scope scope) throws ScriptPlanningException {
        SimpleExecutionTree.Builder builder = SimpleExecutionTree.builderForScriptNamed(script.getName());
        for(Statement statement : script.getStatements()){
            addStepForStatement(statement, scope, builder);
        }
        return new SimpleExecutionPlan(builder.build());
    }

    private static void addStepForStatement(Statement statement, final Scope scope, final SimpleExecutionTree.Builder builder) throws ScriptPlanningException {
        final Scope innerScope = scope.copy();
        final List<ScriptPlanningException> exceptions = Lists.newArrayList();
        statement.accept(new StatementVisitor() {
            @Override
            public void visit(MethodInvocation invocation) {
                try {
                    evaluateMethodInvocation(invocation, innerScope, builder);
                } catch (ScriptPlanningException e) {
                    exceptions.add(e);
                }
            }

            @Override
            public void visit(RemoteInvocation invocation) {
                try {
                    evaluateRemoteMethodInvocation(invocation, innerScope,builder);
                } catch (ScriptPlanningException e) {
                    exceptions.add(e);
                }
            }
        });
        if(!exceptions.isEmpty()){
            throw exceptions.iterator().next();
        }
    }

    private static ImmutableList<ParameterDescription> getParameterValues(Statement invocation, final Scope innerScope, Parameterized parameterized) throws ScriptPlanningException {
        final ImmutableList.Builder<ParameterDescription> descriptions = ImmutableList.builder();
        final Iterator<Expression> expressions = invocation.getParameterExpressions().iterator();
        for(final ParameterDef def: parameterized.getParameters()){
            if(expressions.hasNext()){
                expressions.next().accept(new ExpressionVisitor() {
                    @Override
                    public void visit(Literal literal) {
                        descriptions.add(ParameterDescription.create(def.getName(), literal.getValue()));
                    }

                    @Override
                    public void visit(Identifier literal) {
                        descriptions.add(ParameterDescription.create(def.getName(), innerScope.getIdentifier(literal.getIdentifierName())));
                    }
                });
            }else{
                throw new ScriptPlanningException("parameters did not match");
            }
        }
        return descriptions.build();
    }

    private static void evaluateMethodInvocation(MethodInvocation invocation, Scope innerScope, SimpleExecutionTree.Builder builder) throws ScriptPlanningException {
        MethodDef methodDef = innerScope.getMethodDef(invocation.getMethodName());
        Iterator<ParameterDef> paramDefs = methodDef.getParameters().iterator();
        Iterator<Expression> invocationDefs = invocation.getParameterExpressions().iterator();

        while(paramDefs.hasNext()){
            addIdentifierToScope(paramDefs.next().getName(),invocationDefs.next(),innerScope);
        }

        ImmutableList<ParameterDescription> parameterValues = getParameterValues(invocation, innerScope, methodDef);
        MethodInvocationStep step = MethodInvocationStep.create(methodDef.getName()).withParameters(parameterValues).build();
        builder.pushMethod(step);
        for(Statement methodStatement : methodDef.getStatements()){
            addStepForStatement(methodStatement, innerScope, builder);
        }
        builder.pop();
    }

    private static void addIdentifierToScope(final String name, Expression expression, final Scope scope) {
        expression.accept(new ExpressionVisitor() {
            @Override
            public void visit(Literal literal) {
                scope.addIdentifier(name,literal.getValue());
            }

            @Override
            public void visit(Identifier identifier) {
                scope.addIdentifier(name,scope.getIdentifier(identifier.getIdentifierName()));
            }
        });
    }


    private static void evaluateRemoteMethodInvocation(RemoteInvocation invocation, final Scope innerScope, SimpleExecutionTree.Builder builder) throws ScriptPlanningException {
        RemoteMethodDef remoteMethodDef = innerScope.getRemoteMethodDef(invocation.getMethodName());
        ImmutableList<ParameterDescription> parameterDescriptions = getParameterValues(invocation, innerScope, remoteMethodDef);
        RemoteMethodInvocationStep.Builder step = RemoteMethodInvocationStep.named(invocation.getMethodName()).withParameters(parameterDescriptions);
        builder.pushRemote(step.build());
        builder.pop();
    }

    public static class Execution{
        private Future<?> executionFuture;
        private ProgressReportProvider progressReportProvider;

        public Execution(Future<?> executionFuture, ProgressReportProvider progressReportProvider) {
            this.executionFuture = executionFuture;
            this.progressReportProvider = progressReportProvider;
        }

        public Future<?> getExecutionFuture() {
            return executionFuture;
        }

        public ProgressReportProvider getProgressReportProvider() {
            return progressReportProvider;
        }
    }
}
