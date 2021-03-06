package org.crashtest.interpreter.model.statements;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import org.crashtest.interpreter.model.Expression;
import org.crashtest.interpreter.model.Statement;
import org.crashtest.interpreter.model.StatementVisitor;

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


    @Override
    public List<Expression> getParameterExpressions() {
        return parameterExpressions;
    }

    public static Builder named(String name){
        return new Builder(name);
    }

    @Override
    public String toString(){
        return Objects.toStringHelper(this).add("methodName",methodName).add("parameterExpressions",parameterExpressions).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(methodName,parameterExpressions);
    }

    @Override
    public boolean equals(final Object obj){
        if(obj instanceof RemoteInvocation){
            final RemoteInvocation other = (RemoteInvocation) obj;
            return Objects.equal(methodName, other.methodName)
                    && Objects.equal(parameterExpressions, other.parameterExpressions);
        } else{
            return false;
        }
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

        public Builder withParameterExpressions(Iterable<Expression> expressions){
            parameterExpression.addAll(expressions);
            return this;
        }

        public RemoteInvocation build(){
            return new RemoteInvocation(name, parameterExpression.build());
        }

    }
}
