package org.crashtest.service.impl;

import org.crashtest.interpreter.ScriptExecutionException;
import org.crashtest.interpreter.ScriptExecutor;
import org.crashtest.interpreter.model.Script;
import org.crashtest.service.*;
import org.crashtest.service.model.ExecutionId;
import org.crashtest.service.model.ScriptId;

public class SimpleScriptExecutorService implements ScriptExecutorService {
    ScopeService scopeService;
    ScriptRepositoryService scriptRepositoryService;
    ScriptExecutionRecorderService scriptExecutionRecorderService;

    private static final SimpleScriptExecutorService instance = new SimpleScriptExecutorService(SimpleScopeService.instance(),SimpleScriptRepositoryService.instance());

    public SimpleScriptExecutorService(ScopeService scopeService, ScriptRepositoryService scriptRepositoryService) {
        this.scopeService = scopeService;
        this.scriptRepositoryService = scriptRepositoryService;
        scriptExecutionRecorderService = ScriptExecutionRecorderService.instance();
    }

    public static SimpleScriptExecutorService getInstance(){
        return instance;
    }

    @Override
    public ExecutionId execute(ScriptId scriptId) throws ScriptExecutionException {
        Script script;
        try {
            script = scriptRepositoryService.getScript(scriptId);
        } catch (NoSuchScriptDefinedException e) {
            throw new ScriptExecutionException("no script defined for id " + scriptId.getId());
        }
        ScriptExecutor service = scopeService.getScriptExecutor();
        ScriptExecutionRecorder recorder = scriptExecutionRecorderService.getNewRecorder();
        service.addListener(recorder);
        service.executeScript(script);
        return recorder.getId();
    }
}
