package org.crashtest.http.server.resources;

import org.crashtest.http.server.request.MethodDefinitionRequest;
import org.crashtest.http.server.response.ErrorResponse;
import org.crashtest.http.server.response.MethodDefinitionResponse;
import org.crashtest.http.serialization.Deserializer;
import org.crashtest.http.serialization.SerializationException;
import org.crashtest.http.serialization.Serializer;
import org.crashtest.http.server.translation.MethodDefinitionTranslator;
import org.crashtest.http.server.translation.Translator;
import org.crashtest.http.server.validation.Validator;
import org.crashtest.interpreter.model.MethodDef;
import org.crashtest.service.ScopeService;
import org.crashtest.service.impl.SimpleScopeService;
import org.crashtest.service.model.MethodId;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import java.io.InputStream;

public class MethodDefinitionResource extends ServerResource {

    private static final String LAST_RESORT = "{\"errors\" : [\"failed to serialize error\"]";
    private Deserializer<MethodDefinitionRequest> deserializer = Deserializer.forClass(MethodDefinitionRequest.class);
    private Serializer<MethodDefinitionResponse> serializer = Serializer.forClass(MethodDefinitionResponse.class);
    private Serializer<ErrorResponse> errorSerializer = Serializer.forClass(ErrorResponse.class);
    private Validator validator = Validator.instance();
    private Translator<MethodDefinitionRequest, MethodDef> translator = new MethodDefinitionTranslator();
    private ScopeService service = SimpleScopeService.instance();

    @Post("json")
    public String define(InputStream document) {
        String response;
        try {
            MethodDefinitionRequest request = deserializer.deserialize(document);
            validator.validate(request);
            MethodDef def = translator.translate(request);
            MethodId methodId = service.defineMethod(def);
            MethodDefinitionResponse methodResponse = MethodDefinitionResponse.forId(methodId);
            response = serializer.serialize(methodResponse);
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
