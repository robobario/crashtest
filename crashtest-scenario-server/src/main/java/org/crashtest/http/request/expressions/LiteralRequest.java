package org.crashtest.http.request.expressions;

import com.google.common.base.Strings;
import org.crashtest.http.request.ExpressionRequestVisitor;
import org.crashtest.http.request.ParameterExpressionRequest;

public class LiteralRequest implements ParameterExpressionRequest{
    private String value;

    @Override
    public void accept(ExpressionRequestVisitor visitor) {
        visitor.visit(this);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean isValid() {
        return !Strings.isNullOrEmpty(value);
    }
}