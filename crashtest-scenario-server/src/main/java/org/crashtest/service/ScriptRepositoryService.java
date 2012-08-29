package org.crashtest.service;

import org.crashtest.interpreter.model.Script;
import org.crashtest.service.model.ScriptId;

import java.util.Collection;

public interface ScriptRepositoryService {
    public ScriptId addScript(Script script) throws ScriptDefinitionException;
    public Script getScript(ScriptId scriptId) throws NoSuchScriptDefinedException;
    Collection<ScriptId> getAllScriptIds();
}
