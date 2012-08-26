package org.crashtest.http.request;

import org.crashtest.http.request.statements.MethodInvocationRequest;
import org.crashtest.http.request.statements.RemoteInvocationRequest;

public interface StatementRequestVisitor {
    public void visit(RemoteInvocationRequest request);
    public void visit(MethodInvocationRequest request);
}
