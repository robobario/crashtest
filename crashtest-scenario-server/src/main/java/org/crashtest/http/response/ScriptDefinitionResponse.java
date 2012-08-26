package org.crashtest.http.response;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.crashtest.service.model.ScriptId;

@JsonAutoDetect
public class ScriptDefinitionResponse {
    private ScriptDefinitionResponse(ScriptId id) {
        this.id = id;
    }
    ScriptId id;

    public String getDetailsPath(){
        return "crashtest/scripts/"+id.getId();
    }

    public String getExecutionPath(){
        return "crashtest/scripts/"+id.getId()+"/execute";
    }

    public static ScriptDefinitionResponse forId(ScriptId id){
        return new ScriptDefinitionResponse(id);
    }
}
