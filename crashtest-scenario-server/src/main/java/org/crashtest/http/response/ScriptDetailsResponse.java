package org.crashtest.http.response;

import com.google.common.collect.ImmutableList;
import org.crashtest.interpreter.model.Script;
import org.crashtest.service.model.ScriptId;

import java.util.List;

public class ScriptDetailsResponse implements Response{
    private Script script;
    private ScriptId id;

    private ScriptDetailsResponse(Script script, ScriptId id) {
        this.script = script;
        this.id = id;
    }

    @Override
    public List<String> getErrors() {
        return ImmutableList.of();
    }

    public ScriptResponse getScript() {
        return ScriptResponse.responseFor(script,id);
    }

    public static ScriptDetailsResponse responseFor(Script script, ScriptId id){
        return new ScriptDetailsResponse(script,id);
    }
}
