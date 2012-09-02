package org.crashtest.service;

import org.crashtest.interpreter.ScriptExecutionException;
import org.crashtest.service.model.ExecutionId;
import org.crashtest.service.model.ScriptId;

public interface ScriptExecutorService {
    public ExecutionId execute(ScriptId scriptId) throws ScriptExecutionException;
}
