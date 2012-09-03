package org.crashtest.interpreter.model.plan;

public interface ExecutionTree {
    ProgressAwareExecutionTreeWalker getNewProgressAwareTreeWalker();
}
