package org.crashtest.http.response;

import com.google.common.collect.ImmutableList;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.crashtest.service.model.ScriptId;

import java.util.List;

@JsonAutoDetect
public class ScriptDefinitionResponse implements Response{
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

    @Override
    public List<String> getErrors() {
        return ImmutableList.of();
    }
}
