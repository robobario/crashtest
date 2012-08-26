package org.crashtest.http.request.statements;

import org.crashtest.http.request.ParameterExpressionRequest;
import org.crashtest.http.request.StatementRequest;
import org.crashtest.http.request.StatementRequestVisitor;

import java.util.List;

public class MethodInvocationRequest implements StatementRequest {
    private List<ParameterExpressionRequest> parameterExpressions;
    private String name;

    @Override
    public void accept(StatementRequestVisitor visitor) {
        visitor.visit(this);
    }

    public List<ParameterExpressionRequest> getParameterExpressions() {
        return parameterExpressions;
    }

    public void setParameterExpressions(List<ParameterExpressionRequest> parameterExpressions) {
        this.parameterExpressions = parameterExpressions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
