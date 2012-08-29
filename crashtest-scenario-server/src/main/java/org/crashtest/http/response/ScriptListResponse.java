package org.crashtest.http.response;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.crashtest.service.model.ScriptId;

import java.util.Collection;
import java.util.List;

@JsonAutoDetect
public class ScriptListResponse implements Response {
    private List<ScriptId> ids;

    private ScriptListResponse(List<ScriptId> ids){
        this.ids = ids;
    }

    public List<String> getAllScriptDetailsUrls(){
        return Lists.transform(ids,new Function<ScriptId, String>() {
            @Override
            public String apply(ScriptId input) {
                return "crashtest/scripts/" + input.getId();
            }
        });
    }

    public static ScriptListResponse ofScriptIds(Collection<ScriptId> ids){
        return new ScriptListResponse(ImmutableList.copyOf(ids));
    }
    @Override
    public List<String> getErrors() {
        return ImmutableList.of();
    }
}
