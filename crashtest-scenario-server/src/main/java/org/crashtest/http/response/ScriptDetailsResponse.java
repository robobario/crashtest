package org.crashtest.http.response;

import com.google.common.collect.ImmutableList;
import org.crashtest.interpreter.model.Script;

import java.util.List;

public class ScriptDetailsResponse implements Response{
    private Script script;

    private ScriptDetailsResponse(Script script) {
        this.script = script;
    }

    @Override
    public List<String> getErrors() {
        return ImmutableList.of();
    }

    public Script getScript() {
        return script;
    }

    public static ScriptDetailsResponse responseFor(Script script){
        return new ScriptDetailsResponse(script);
    }
}
