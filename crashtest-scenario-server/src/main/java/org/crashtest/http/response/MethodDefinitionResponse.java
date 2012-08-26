package org.crashtest.http.response;

import org.crashtest.service.model.MethodId;

public class MethodDefinitionResponse {
    private MethodDefinitionResponse(MethodId id) {
        this.id = id;
    }
    MethodId id;

    public String getRelativePath(){
        return "crashtest/methods/"+id.getId();
    }

    public static MethodDefinitionResponse forId(MethodId id){
        return new MethodDefinitionResponse(id);
    }
}
