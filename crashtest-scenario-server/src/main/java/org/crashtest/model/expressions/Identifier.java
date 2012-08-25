package org.crashtest.model.expressions;

import org.crashtest.model.Expression;
import org.crashtest.model.ExpressionVisitor;

public class Identifier implements Expression {
    public String getIdentifierName() {
        return identifierName;
    }

    String identifierName;

    public Identifier(String identifierName) {
        this.identifierName = identifierName;
    }

    @Override
    public void accept(ExpressionVisitor visitor) {
        visitor.visit(this);
    }
}
