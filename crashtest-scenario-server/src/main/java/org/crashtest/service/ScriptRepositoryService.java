package org.crashtest.service;

import org.crashtest.interpreter.model.Script;
import org.crashtest.service.model.ScriptId;

public interface ScriptRepositoryService {
    public ScriptId addScript(Script script) throws ScriptDefinitionException;
    public Script getScript(ScriptId scriptId) throws NoSuchScriptDefinedException;
}
