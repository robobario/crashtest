package org.crashtest.service.impl;

import org.crashtest.interpreter.ScriptExecutionException;
import org.crashtest.interpreter.ScriptExecutor;
import org.crashtest.interpreter.model.Script;
import org.crashtest.service.NoSuchScriptDefinedException;
import org.crashtest.service.ScopeService;
import org.crashtest.service.ScriptExecutorService;
import org.crashtest.service.ScriptRepositoryService;
import org.crashtest.service.model.ScriptId;

public class SimpleScriptExecutorService implements ScriptExecutorService {
    ScopeService scopeService;
    ScriptRepositoryService scriptRepositoryService;

    private static final SimpleScriptExecutorService instance = new SimpleScriptExecutorService(SimpleScopeService.instance(),SimpleScriptRepositoryService.instance());

    public SimpleScriptExecutorService(ScopeService scopeService, ScriptRepositoryService scriptRepositoryService) {
        this.scopeService = scopeService;
        this.scriptRepositoryService = scriptRepositoryService;
    }

    public static SimpleScriptExecutorService getInstance(){
        return instance;
    }

    @Override
    public void execute(ScriptId scriptId) throws ScriptExecutionException {
        Script script = null;
        try {
            script = scriptRepositoryService.getScript(scriptId);
        } catch (NoSuchScriptDefinedException e) {
            throw new ScriptExecutionException("no script defined for id " + scriptId.getId());
        }
        ScriptExecutor service = scopeService.getScriptExecutor();
        service.executeScript(script);
    }
}
