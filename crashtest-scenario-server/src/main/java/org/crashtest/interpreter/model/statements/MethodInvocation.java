package org.crashtest.interpreter.model.statements;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import org.crashtest.interpreter.model.Expression;
import org.crashtest.interpreter.model.Statement;
import org.crashtest.interpreter.model.StatementVisitor;

import java.util.List;

public class MethodInvocation implements Statement {

    private String methodName;
    private List<Expression> parameterExpressions;

    private MethodInvocation(String name, List<Expression> parameterExpressions){
        this.methodName = name;
        this.parameterExpressions = parameterExpressions;
    }

    public String getMethodName() {
        return methodName;
    }

    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString(){
        return Objects.toStringHelper(this).add("methodName",methodName).add("parameterExpressions",parameterExpressions).toString();
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

        public Builder withParameterExpressions(Iterable<Expression> expressions){
            parameterExpression.addAll(expressions);
            return this;
        }

        public MethodInvocation build(){
            return new MethodInvocation(name, parameterExpression.build());
        }


    }
}
