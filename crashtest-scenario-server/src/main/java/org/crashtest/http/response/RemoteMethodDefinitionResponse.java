package org.crashtest.http.response;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.crashtest.service.model.RemoteMethodId;

@JsonAutoDetect
public class RemoteMethodDefinitionResponse {
    private RemoteMethodDefinitionResponse(RemoteMethodId id) {
        this.id = id;
    }
    RemoteMethodId id;

    public String getRelativePath(){
        return "crashtest/remote-methods/"+id.getId();
    }

    public static RemoteMethodDefinitionResponse forId(RemoteMethodId id){
        return new RemoteMethodDefinitionResponse(id);
    }
}
