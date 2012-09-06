package org.crashtest.http.server.response;

import com.google.common.collect.ImmutableList;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.crashtest.service.model.RemoteMethodId;

import java.util.List;

@JsonAutoDetect
public class RemoteMethodDefinitionResponse implements Response{
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

    @Override
    public List<String> getErrors() {
        return ImmutableList.of();
    }
}
