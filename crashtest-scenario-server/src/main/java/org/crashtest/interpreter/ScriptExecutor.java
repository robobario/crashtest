package org.crashtest.interpreter;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.crashtest.interpreter.model.*;
import org.crashtest.interpreter.model.expressions.Identifier;
import org.crashtest.interpreter.model.expressions.Literal;
import org.crashtest.interpreter.model.statements.MethodInvocation;
import org.crashtest.interpreter.model.statements.RemoteInvocation;
import org.crashtest.service.RemoteInvokerService;
import org.crashtest.service.ScriptExecutionListener;
import org.crashtest.service.model.ParameterDescription;
import org.crashtest.service.model.RemoteInvocationDescription;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ScriptExecutor {
    RemoteInvokerService service;
    private Scope scope;
    private Collection<ScriptExecutionListener> listeners = ImmutableSet.of();

    public ScriptExecutor(RemoteInvokerService service, Scope scope) {
        this.service = service;
        this.scope = scope;
    }

    public void executeScript(Script script) throws ScriptExecutionException{
        for(Statement statement: script.getStatements()){
            evaluateStatement(statement,scope);
        }
    }

    private void evaluateStatement(Statement statement, final Scope scope) throws ScriptExecutionException {
        final Scope innerScope = scope.copy();
        final List<ScriptExecutionException> exceptions = Lists.newArrayList();
        statement.accept(new StatementVisitor() {
            @Override
            public void visit(MethodInvocation invocation) {
                try {
                    evaluateMethodInvocation(invocation, innerScope);
                } catch (ScriptExecutionException e) {
                    exceptions.add(e);
                }
            }

            @Override
            public void visit(RemoteInvocation invocation) {
                try {
                    evaluateRemoteMethodInvocation(invocation, innerScope);
                } catch (ScriptExecutionException e) {
                    exceptions.add(e);
                    notifyListenersOfError(e);
                }
            }
        });
        if(!exceptions.isEmpty()){
            throw exceptions.iterator().next();
        }
    }

    private void evaluateRemoteMethodInvocation(RemoteInvocation invocation, final Scope innerScope) throws ScriptExecutionException {
        RemoteMethodDef remoteMethodDef = innerScope.getRemoteMethodDef(invocation.getMethodName());
        RemoteInvocationDescription.Builder descriptionBuilder = RemoteInvocationDescription.named(invocation.getMethodName());
        ImmutableList<ParameterDescription> parameterDescriptions = getParameterValues(invocation, innerScope, remoteMethodDef);
        descriptionBuilder.withParameters(parameterDescriptions);
        service.invoke(descriptionBuilder.build());
        notifyListenersOfSuccess(descriptionBuilder.build());
    }

    private void notifyListenersOfSuccess(RemoteInvocationDescription build) {
        for(ScriptExecutionListener listener:listeners){
            listener.onRemoteMethodInvocationSuccess(build);
        }
    }

    private void notifyListenersOfError(ScriptExecutionException exception) {
        for(ScriptExecutionListener listener:listeners){
            listener.onRemoteMethodInvocationError(exception.toString());
        }
    }

    private ImmutableList<ParameterDescription> getParameterValues(RemoteInvocation invocation, final Scope innerScope, RemoteMethodDef remoteMethodDef) throws ScriptExecutionException {
        final ImmutableList.Builder<ParameterDescription> descriptions = ImmutableList.builder();
        final Iterator<Expression> expressions = invocation.getParameterExpressions().iterator();
        for(final ParameterDef def: remoteMethodDef.getParameters()){
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
                  throw new ScriptExecutionException("parameters did not match");
             }
        }
        return descriptions.build();
    }

    private void evaluateMethodInvocation(MethodInvocation invocation, Scope innerScope) throws ScriptExecutionException {
        MethodDef methodDef = innerScope.getMethodDef(invocation.getMethodName());
        Iterator<ParameterDef> paramDefs = methodDef.getParameters().iterator();
        Iterator<Expression> invocationDefs = invocation.getParameterExpressions().iterator();

        while(paramDefs.hasNext()){
            addIdentifierToScope(paramDefs.next().getName(),invocationDefs.next(),innerScope);
        }

        for(Statement methodStatement : methodDef.getStatements()){
            evaluateStatement(methodStatement,innerScope);
        }
    }

    private void addIdentifierToScope(final String name, Expression expression, final Scope scope) {
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

    public void addListener(ScriptExecutionListener listener) {
        listeners = ImmutableSet.<ScriptExecutionListener>builder().addAll(listeners).add(listener).build();
    }
}
