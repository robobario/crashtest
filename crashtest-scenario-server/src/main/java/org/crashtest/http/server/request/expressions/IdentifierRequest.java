package org.crashtest.http.server.request.expressions;

import com.google.common.base.Strings;
import org.crashtest.http.server.request.ExpressionRequestVisitor;
import org.crashtest.http.server.request.ParameterExpressionRequest;

public class IdentifierRequest implements ParameterExpressionRequest {
    private String name;

    @Override
    public void accept(ExpressionRequestVisitor visitor) {
        visitor.visit(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isValid() {
        return !Strings.isNullOrEmpty(name);
    }
}
