package org.crashtest.interpreter.model;

public interface Expression {
    void accept(ExpressionVisitor visitor);
}
