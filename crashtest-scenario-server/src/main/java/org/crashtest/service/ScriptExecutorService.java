package org.crashtest.service;

import org.crashtest.interpreter.ScriptExecutionException;
import org.crashtest.service.model.ScriptId;

public interface ScriptExecutorService {
    public void execute(ScriptId scriptId) throws ScriptExecutionException;
}
