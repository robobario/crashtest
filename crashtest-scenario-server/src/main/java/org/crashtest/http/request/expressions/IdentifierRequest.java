package org.crashtest.http.request.expressions;

import org.crashtest.http.request.ExpressionRequestVisitor;
import org.crashtest.http.request.ParameterExpressionRequest;

public class IdentifierRequest implements ParameterExpressionRequest {
    private String name;

    @Override
    public void accept(ExpressionRequestVisitor visitor) {
        visitor.visit(this);
    }

    public String getName() {
        return name;
    }
}
