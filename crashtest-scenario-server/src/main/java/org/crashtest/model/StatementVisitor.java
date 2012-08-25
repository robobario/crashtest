package org.crashtest.model;

import org.crashtest.model.statements.MethodInvocation;
import org.crashtest.model.statements.RemoteInvocation;

public interface StatementVisitor {
    void visit(MethodInvocation invocation);
    void visit(RemoteInvocation invocation);
}
