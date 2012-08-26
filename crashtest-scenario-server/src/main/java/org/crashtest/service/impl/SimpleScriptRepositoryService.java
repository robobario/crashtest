package org.crashtest.service.impl;

import com.google.common.collect.ImmutableMap;
import org.crashtest.interpreter.model.Script;
import org.crashtest.interpreter.model.Statement;
import org.crashtest.service.NoSuchScriptDefinedException;
import org.crashtest.service.ScopeService;
import org.crashtest.service.ScriptDefinitionException;
import org.crashtest.service.ScriptRepositoryService;
import org.crashtest.service.model.ScriptId;

public class SimpleScriptRepositoryService implements ScriptRepositoryService{
    ScopeService service;
    ImmutableMap<ScriptId, Script> scriptIdToScript = ImmutableMap.of();
    ImmutableMap<String, Script> scriptNameToScript = ImmutableMap.of();
    private static final SimpleScriptRepositoryService instance = new SimpleScriptRepositoryService(SimpleScopeService.instance());

    public SimpleScriptRepositoryService(ScopeService service) {
        this.service = service;
    }

    @Override
    public synchronized ScriptId addScript(Script script) throws ScriptDefinitionException {
        if(scriptNameToScript.containsKey(script.getName())){
            throw new ScriptDefinitionException("a script with that name is already defined");
        }
        for(Statement statement :script.getStatements()){
            if(!service.isStatementDefined(statement)){
                throw new ScriptDefinitionException("statement does not call a defined method :"+ statement);
            }
        }
        ScriptId nextId = ScriptId.of(scriptIdToScript.size() + 1);
        scriptIdToScript = ImmutableMap.<ScriptId,Script>builder().putAll(scriptIdToScript).put(nextId,script).build();
        scriptNameToScript = ImmutableMap.<String,Script>builder().putAll(scriptNameToScript).put(script.getName(),script).build();
        return nextId;
    }

    @Override
    public Script getScript(ScriptId scriptId) throws NoSuchScriptDefinedException{
        if(scriptIdToScript.containsKey(scriptId)){
            return scriptIdToScript.get(scriptId);
        }else{
            throw new NoSuchScriptDefinedException("no script with id " + scriptId.getId() + " is defined");
        }
    }

    public static ScriptRepositoryService instance() {
        return instance;
    }
}
