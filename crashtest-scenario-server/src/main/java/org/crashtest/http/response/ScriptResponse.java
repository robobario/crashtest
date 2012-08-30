package org.crashtest.http.response;

import org.crashtest.interpreter.model.Script;
import org.crashtest.service.model.ScriptId;

public class ScriptResponse {
    private final Script script;
    private final ScriptId id;

    private ScriptResponse(Script script, ScriptId id) {
        this.script = script;
        this.id = id;
    }

    public String getName() {
        return script.getName();
    }

    public String getExecutionUrl() {
        return "crashtest/scripts/" + id.getId() + "/execute";
    }

    public static ScriptResponse responseFor(Script script, ScriptId id){
        return new ScriptResponse(script,id);
    }
}
