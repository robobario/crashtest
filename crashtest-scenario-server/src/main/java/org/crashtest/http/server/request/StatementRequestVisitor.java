package org.crashtest.http.server.request;

import org.crashtest.http.server.request.statements.MethodInvocationRequest;
import org.crashtest.http.server.request.statements.RemoteInvocationRequest;

public interface StatementRequestVisitor {
    public void visit(RemoteInvocationRequest request);
    public void visit(MethodInvocationRequest request);
}
