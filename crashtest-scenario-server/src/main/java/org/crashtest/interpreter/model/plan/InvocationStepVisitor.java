package org.crashtest.interpreter.model.plan;

import org.crashtest.interpreter.model.plan.descriptions.MethodInvocationStep;
import org.crashtest.interpreter.model.plan.descriptions.RemoteMethodInvocationStep;
import org.crashtest.interpreter.model.plan.descriptions.ScriptInvocationStep;

public interface InvocationStepVisitor {
    void visit(MethodInvocationStep step);
    void visit(RemoteMethodInvocationStep step);
    void visit(ScriptInvocationStep step);
}
