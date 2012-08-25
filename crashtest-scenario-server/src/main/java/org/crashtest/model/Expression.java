package org.crashtest.model;

public interface Expression {
    void accept(ExpressionVisitor visitor);
}
