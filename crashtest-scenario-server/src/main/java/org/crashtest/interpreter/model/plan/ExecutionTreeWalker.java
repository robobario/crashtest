package org.crashtest.interpreter.model.plan;


public interface ExecutionTreeWalker {
    public void depthFirst(InvocationStepVisitor visitor);
}
