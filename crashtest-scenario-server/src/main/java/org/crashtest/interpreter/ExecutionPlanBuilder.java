package org.crashtest.interpreter;

import org.crashtest.interpreter.model.Script;

public interface ExecutionPlanBuilder {
    ExecutionPlan buildPlanFor(Script script);
}
