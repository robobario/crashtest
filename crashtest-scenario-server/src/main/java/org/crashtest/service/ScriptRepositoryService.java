package org.crashtest.service;

import org.crashtest.interpreter.model.Script;

public interface ScriptRepositoryService {
    public void addScript(Script script) throws ScriptDefinitionException;
    public Script getScript(String scriptName);
}
