package org.crashtest.interpreter;

import org.crashtest.interpreter.model.Script;
import org.crashtest.interpreter.model.plan.SimpleExecutionPlan;

public interface ScriptExecutor {
    SimpleExecutionPlan.Execution executeScript(Script script) throws ScriptExecutionException;
}
