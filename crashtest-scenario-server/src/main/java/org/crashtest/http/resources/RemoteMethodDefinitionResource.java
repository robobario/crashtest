package org.crashtest.http.resources;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import org.codehaus.jackson.map.ObjectMapper;
import org.crashtest.http.request.ParameterRequest;
import org.crashtest.http.request.RemoteMethodDefinitionRequest;
import org.crashtest.http.response.RemoteMethodDefinitionResponse;
import org.crashtest.interpreter.model.ParameterDef;
import org.crashtest.interpreter.model.RemoteMethodDef;
import org.crashtest.service.model.RemoteMethodId;
import org.crashtest.service.ScopeService;
import org.crashtest.service.impl.SimpleScopeService;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RemoteMethodDefinitionResource extends ServerResource{

    private ObjectMapper mapper;

    ScopeService service = SimpleScopeService.instance();

    public RemoteMethodDefinitionResource() {
        mapper = new ObjectMapper();
    }

    @Post("json")
    public String define(InputStream document){
        RemoteMethodDefinitionRequest definitionRequest;
        try {
            definitionRequest = mapper.readValue(new InputStreamReader(document), RemoteMethodDefinitionRequest.class);
        } catch (IOException e) {
            return "fail - io exception " + e;
        }
        if(!definitionRequest.isValid()){
            return "fail - invalid request";
        }
        RemoteMethodDef.Builder definition = RemoteMethodDef.named(definitionRequest.getName());
        definition.withParameterDefs(Iterables.transform(definitionRequest.getParameters(), new Function<ParameterRequest, ParameterDef>() {
            @Override
            public ParameterDef apply(ParameterRequest input) {
                return ParameterDef.named(input.getName());
            }
        }));
        try {
            RemoteMethodId remoteMethodId = service.defineRemoteMethod(definition.build());
            return mapper.writeValueAsString(RemoteMethodDefinitionResponse.forId(remoteMethodId));
        } catch (Exception e) {
            return "fail - " + e;
        }
    }
}
