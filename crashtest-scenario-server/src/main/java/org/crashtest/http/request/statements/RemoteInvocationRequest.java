package org.crashtest.http.request.statements;

import org.crashtest.http.request.ParameterExpressionRequest;
import org.crashtest.http.request.StatementRequest;
import org.crashtest.http.request.StatementRequestVisitor;

import java.util.List;

public class RemoteInvocationRequest implements StatementRequest {
    private String name;
    private List<ParameterExpressionRequest> expressions;

    @Override
    public void accept(StatementRequestVisitor visitor) {
        visitor.visit(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ParameterExpressionRequest> getExpressions() {
        return expressions;
    }
}
