package org.crashtest.model;

import org.crashtest.model.expressions.Identifier;
import org.crashtest.model.expressions.Literal;

public interface ExpressionVisitor {
    void visit(Literal literal);
    void visit(Identifier literal);
}
