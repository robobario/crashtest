package org.crashtest.model.expressions;

import org.crashtest.model.Expression;
import org.crashtest.model.ExpressionVisitor;

public class Literal implements Expression {
    public String getValue() {
        return value;
    }

    String value;

    public Literal(String value) {
        this.value = value;
    }

    @Override
    public void accept(ExpressionVisitor visitor) {
        visitor.visit(this);
    }
}
