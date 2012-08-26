package org.crashtest.http.response;

import com.google.common.collect.ImmutableList;
import org.crashtest.interpreter.model.RemoteMethodDef;

import java.util.List;

public class RemoteMethodDetailsResponse implements Response{
    private RemoteMethodDef methodDefinition;

    private RemoteMethodDetailsResponse(RemoteMethodDef methodDefinition) {
        this.methodDefinition = methodDefinition;
    }

    @Override
    public List<String> getErrors() {
        return ImmutableList.of();
    }

    public RemoteMethodDef getMethodDefinition() {
        return methodDefinition;
    }

    public static RemoteMethodDetailsResponse responseFor(RemoteMethodDef def){
        return new RemoteMethodDetailsResponse(def);
    }
}
