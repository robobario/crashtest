package org.crashtest.http.response;


import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.crashtest.service.model.RemoteMethodId;

import java.util.List;

@JsonAutoDetect
public class RemoteMethodDetailsListResponse implements Response {

    private List<RemoteMethodId> defs;

    private RemoteMethodDetailsListResponse(List<RemoteMethodId> defs){
        this.defs = defs;
    }

    public List<String> getRemoteMethodDetails(){
        return Lists.transform(defs, new Function<RemoteMethodId, String>() {
            @Override
            public String apply(RemoteMethodId input) {
                return "crashtest/remote-methods/"+input.getId();
            }
        });
    }

    @Override
    public List<String> getErrors() {
        return ImmutableList.of();
    }

    public static RemoteMethodDetailsListResponse forDefinitionIds(List<RemoteMethodId> defs){
        return new RemoteMethodDetailsListResponse(defs);
    }
}
