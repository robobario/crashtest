package org.crashtest.http.request;

public interface ParameterExpressionRequest {
    public void accept(ExpressionRequestVisitor visitor);
}
