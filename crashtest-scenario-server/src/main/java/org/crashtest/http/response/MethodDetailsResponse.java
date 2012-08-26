package org.crashtest.http.response;

import com.google.common.collect.ImmutableList;
import org.crashtest.interpreter.model.MethodDef;

import java.util.List;

public class MethodDetailsResponse implements Response{
    private MethodDef methodDefinition;

    private MethodDetailsResponse(MethodDef methodDefinition) {
        this.methodDefinition = methodDefinition;
    }

    @Override
    public List<String> getErrors() {
        return ImmutableList.of();
    }

    public MethodDef getMethodDefinition() {
        return methodDefinition;
    }

    public static MethodDetailsResponse responseFor(MethodDef def){
        return new MethodDetailsResponse(def);
    }
}
