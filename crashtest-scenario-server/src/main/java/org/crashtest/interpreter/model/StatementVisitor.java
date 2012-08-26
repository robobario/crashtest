package org.crashtest.interpreter.model;

import org.crashtest.interpreter.model.statements.MethodInvocation;
import org.crashtest.interpreter.model.statements.RemoteInvocation;

public interface StatementVisitor {
    void visit(MethodInvocation invocation);
    void visit(RemoteInvocation invocation);
}
