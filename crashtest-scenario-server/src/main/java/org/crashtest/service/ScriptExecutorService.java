package org.crashtest.service;

import org.crashtest.interpreter.ScriptExecutionException;

public interface ScriptExecutorService {
    public void execute(String scriptName) throws ScriptExecutionException;
}
