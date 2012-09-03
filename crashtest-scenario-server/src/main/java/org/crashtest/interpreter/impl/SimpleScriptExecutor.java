package org.crashtest.interpreter.impl;

import org.crashtest.interpreter.Scope;
import org.crashtest.interpreter.ScriptExecutionException;
import org.crashtest.interpreter.ScriptExecutor;
import org.crashtest.interpreter.model.Script;
import org.crashtest.interpreter.model.plan.ScriptPlanningException;
import org.crashtest.interpreter.model.plan.SimpleExecutionPlan;
import org.crashtest.service.RemoteInvokerService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleScriptExecutor implements ScriptExecutor {
    RemoteInvokerService service;
    private Scope scope;
    ExecutorService executorService = Executors.newCachedThreadPool();

    public SimpleScriptExecutor(RemoteInvokerService service, Scope scope) {
        this.service = service;
        this.scope = scope;
    }

    @Override
    public SimpleExecutionPlan.Execution executeScript(Script script) throws ScriptExecutionException {
        try {
            return SimpleExecutionPlan.planFor(script, scope).execute(service, executorService);

        } catch (ScriptPlanningException e) {
            throw new ScriptExecutionException(e);
        }
    }
}
