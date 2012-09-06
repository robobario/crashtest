package org.crashtest.http.server.resources;

import org.crashtest.http.server.request.RemoteMethodDefinitionRequest;
import org.crashtest.http.server.response.ErrorResponse;
import org.crashtest.http.server.response.RemoteMethodDefinitionResponse;
import org.crashtest.http.serialization.Deserializer;
import org.crashtest.http.serialization.SerializationException;
import org.crashtest.http.serialization.Serializer;
import org.crashtest.http.server.translation.RemoteMethodDefinitionTranslator;
import org.crashtest.http.server.translation.Translator;
import org.crashtest.http.server.validation.Validator;
import org.crashtest.interpreter.model.RemoteMethodDef;
import org.crashtest.service.ScopeService;
import org.crashtest.service.impl.SimpleScopeService;
import org.crashtest.service.model.RemoteMethodId;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import java.io.InputStream;

public class RemoteMethodDefinitionResource extends ServerResource {
    private static final String LAST_RESORT = "{\"errors\" : [\"failed to serialize error\"]";
    private Deserializer<RemoteMethodDefinitionRequest> deserializer = Deserializer.forClass(RemoteMethodDefinitionRequest.class);
    private Serializer<RemoteMethodDefinitionResponse> serializer = Serializer.forClass(RemoteMethodDefinitionResponse.class);
    private Serializer<ErrorResponse> errorSerializer = Serializer.forClass(ErrorResponse.class);
    private Validator validator = Validator.instance();
    private ScopeService service = SimpleScopeService.instance();
    private Translator<RemoteMethodDefinitionRequest, RemoteMethodDef> translator = new RemoteMethodDefinitionTranslator();

    @Post("json")
    public String define(InputStream document) {
        String response;
        try {
            RemoteMethodDefinitionRequest request = deserializer.deserialize(document);
            validator.validate(request);
            RemoteMethodDef remoteDef = translator.translate(request);
            RemoteMethodId remoteMethodId = service.defineRemoteMethod(remoteDef);
            response = serializer.serialize(RemoteMethodDefinitionResponse.forId(remoteMethodId));
        } catch (Exception e) {
            try {
                response = errorSerializer.serialize(ErrorResponse.forException(e));
            } catch (SerializationException e1) {
                response = LAST_RESORT;
            }
        }
        return response;
    }
}
