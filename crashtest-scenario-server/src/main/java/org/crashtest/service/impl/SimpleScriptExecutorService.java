package org.crashtest.service.impl;

import org.crashtest.interpreter.Scope;
import org.crashtest.interpreter.ScriptExecutionException;
import org.crashtest.interpreter.ScriptExecutor;
import org.crashtest.interpreter.model.Script;
import org.crashtest.service.*;
import org.mockito.Mockito;

public class SimpleScriptExecutorService implements ScriptExecutorService {
    ScriptExecutor executor = new ScriptExecutor(Mockito.mock(RemoteInvokerService.class));
    ScopeService scopeService;
    ScriptRepositoryService scriptRepositoryService;

    public SimpleScriptExecutorService(ScopeService scopeService, ScriptRepositoryService scriptRepositoryService) {
        this.scopeService = scopeService;
        this.scriptRepositoryService = scriptRepositoryService;
    }

    @Override
    public void execute(String scriptName) throws ScriptExecutionException {
        Script script = scriptRepositoryService.getScript(scriptName);
        if(null == script){
            throw new ScriptExecutionException("no script with that name is available");
        }
        Scope globalScope = scopeService.getGlobalScope();
        if(null == globalScope){
            throw new ScriptExecutionException("no global scope was available");
        }
        executor.executeScript(script,globalScope);
    }
}
