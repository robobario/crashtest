package org.crashtest.http.request;

import org.crashtest.http.request.expressions.IdentifierRequest;
import org.crashtest.http.request.expressions.LiteralRequest;

public interface ExpressionRequestVisitor {
    public void visit(LiteralRequest request);
    public void visit(IdentifierRequest request);
}
