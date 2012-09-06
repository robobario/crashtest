package org.crashtest.http.server.request;

import org.crashtest.http.server.request.expressions.IdentifierRequest;
import org.crashtest.http.server.request.expressions.LiteralRequest;

public interface ExpressionRequestVisitor {
    public void visit(LiteralRequest request);
    public void visit(IdentifierRequest request);
}
