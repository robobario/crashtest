package org.crashtest.model.statements;

import com.google.common.collect.ImmutableList;
import org.crashtest.model.Expression;
import org.crashtest.model.Statement;
import org.crashtest.model.StatementVisitor;

import java.util.List;

public class RemoteInvocation implements Statement {
    private String methodName;
    private List<Expression> parameterExpressions;

    private RemoteInvocation(String methodName, List<Expression> parameterExpressions){
       this.methodName = methodName;
        this.parameterExpressions = parameterExpressions;
    }

    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visit(this);
    }


    public String getMethodName() {
        return methodName;
    }


    public List<Expression> getParameterExpressions() {
        return parameterExpressions;
    }

    public static Builder named(String name){
        return new Builder(name);
    }

    public static class Builder {
        private String name;
        private ImmutableList.Builder<Expression> parameterExpression = ImmutableList.builder();
        private Builder(String name){
           this.name = name;
        }

        public Builder withParameterExpression(Expression expression){
            parameterExpression.add(expression);
            return this;
        }

        public Builder withParameterExpressions(List<Expression> expressions){
            parameterExpression.addAll(expressions);
            return this;
        }

        public RemoteInvocation build(){
            return new RemoteInvocation(name, parameterExpression.build());
        }


    }
}
