package org.crashtest.interpreter;

import com.google.common.collect.ImmutableList;
import org.crashtest.model.*;
import org.crashtest.model.expressions.Identifier;
import org.crashtest.model.expressions.Literal;
import org.crashtest.model.statements.MethodInvocation;
import org.crashtest.model.statements.RemoteInvocation;
import org.crashtest.service.ParameterDescription;
import org.crashtest.service.RemoteInvocationDescription;
import org.crashtest.service.RemoteInvokerService;

import java.util.Iterator;

public class ScriptExecutor {
    RemoteInvokerService service;

    public ScriptExecutor(RemoteInvokerService service) {
        this.service = service;
    }

    public void executeScript(Script script, Scope scope){
        for(Statement statement: script.getStatements()){
            evaluateStatement(statement, scope);
        }
    }

    private void evaluateStatement(Statement statement, final Scope scope) {
        final Scope innerScope = scope.copy();
        statement.accept(new StatementVisitor() {
            @Override
            public void visit(MethodInvocation invocation) {
                evaluateMethodInvocation(invocation, innerScope);
            }

            @Override
            public void visit(RemoteInvocation invocation) {
                evaluateRemoteMethodInvocation(invocation, innerScope);
            }
        });
    }

    private void evaluateRemoteMethodInvocation(RemoteInvocation invocation, final Scope innerScope) {
        RemoteMethodDef remoteMethodDef = innerScope.getRemoteMethodDef(invocation.getMethodName());
        RemoteInvocationDescription.Builder descriptionBuilder = RemoteInvocationDescription.named(invocation.getMethodName());
        ImmutableList<ParameterDescription> parameterDescriptions = getParameterValues(invocation, innerScope, remoteMethodDef);
        descriptionBuilder.withParameters(parameterDescriptions);
        service.invoke(descriptionBuilder.build());
    }

    private ImmutableList<ParameterDescription> getParameterValues(RemoteInvocation invocation, final Scope innerScope, RemoteMethodDef remoteMethodDef) {
        final ImmutableList.Builder<ParameterDescription> descriptions = ImmutableList.builder();
        final Iterator<ParameterDef> iterator = remoteMethodDef.getParameters().iterator();
        for(Expression expression : invocation.getParameterExpressions()){
            expression.accept(new ExpressionVisitor() {
                @Override
                public void visit(Literal literal) {
                    descriptions.add(ParameterDescription.create(iterator.next().getName(), literal.getValue()));
                }

                @Override
                public void visit(Identifier literal) {
                    descriptions.add(ParameterDescription.create(iterator.next().getName(), innerScope.getIdentifier(literal.getIdentifierName())));
                }
            });
        }
        return descriptions.build();
    }

    private void evaluateMethodInvocation(MethodInvocation invocation, Scope innerScope) {
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
}
