package org.crashtest.service.impl;

import com.google.common.collect.ImmutableMap;
import org.crashtest.interpreter.model.Script;
import org.crashtest.interpreter.model.Statement;
import org.crashtest.service.ScopeService;
import org.crashtest.service.ScriptDefinitionException;
import org.crashtest.service.ScriptRepositoryService;

public class SimpleScriptRepositoryService implements ScriptRepositoryService{
    ScopeService service;
    ImmutableMap<String, Script> scriptNameToScript = ImmutableMap.of();

    public SimpleScriptRepositoryService(ScopeService service) {
        this.service = service;
    }


    @Override
    public void addScript(Script script) throws ScriptDefinitionException {
        for(Statement statement :script.getStatements()){
            if(!service.isStatementDefined(statement)){
                throw new ScriptDefinitionException("statement does not call a defined method :"+ statement);
            }
        }
        scriptNameToScript = ImmutableMap.<String,Script>builder().putAll(scriptNameToScript).put(script.getName(),script).build();
    }

    @Override
    public Script getScript(String scriptName) {
         return scriptNameToScript.get(scriptName);
    }
}
