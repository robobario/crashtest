package org.crashtest.http.response;

import com.google.common.collect.ImmutableList;
import org.crashtest.service.model.MethodId;

import java.util.List;

public class MethodDefinitionResponse implements Response {

    MethodId id;

    private MethodDefinitionResponse(MethodId id) {
        this.id = id;
    }

    public String getRelativePath(){
        return "crashtest/methods/"+id.getId();
    }

    public static MethodDefinitionResponse forId(MethodId id){
        return new MethodDefinitionResponse(id);
    }

    @Override
    public List<String> getErrors() {
        return ImmutableList.of();
    }
}
