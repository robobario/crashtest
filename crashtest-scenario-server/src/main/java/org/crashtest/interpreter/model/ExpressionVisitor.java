package org.crashtest.interpreter.model;

import org.crashtest.interpreter.model.expressions.Identifier;
import org.crashtest.interpreter.model.expressions.Literal;

public interface ExpressionVisitor {
    void visit(Literal literal);
    void visit(Identifier literal);
}
