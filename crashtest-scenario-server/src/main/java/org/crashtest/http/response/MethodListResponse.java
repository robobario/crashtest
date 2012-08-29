package org.crashtest.http.response;


import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.crashtest.service.model.MethodId;

import java.util.List;

@JsonAutoDetect
public class MethodListResponse implements Response {

    private List<MethodId> defs;

    private MethodListResponse(List<MethodId> defs){
        this.defs = defs;
    }

    public List<String> getMethodDetails(){
        return Lists.transform(defs, new Function<MethodId, String>() {
            @Override
            public String apply(MethodId input) {
                return "crashtest/methods/"+input.getId();
            }
        });
    }

    @Override
    public List<String> getErrors() {
        return ImmutableList.of();
    }

    public static MethodListResponse forDefinitionIds(List<MethodId> defs){
        return new MethodListResponse(defs);
    }
}
