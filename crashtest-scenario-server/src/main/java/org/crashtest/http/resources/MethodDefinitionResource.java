package org.crashtest.http.resources;

import org.crashtest.http.request.MethodDefinitionRequest;
import org.crashtest.http.response.ErrorResponse;
import org.crashtest.http.response.MethodDefinitionResponse;
import org.crashtest.http.serialization.Deserializer;
import org.crashtest.http.serialization.SerializationException;
import org.crashtest.http.serialization.Serializer;
import org.crashtest.http.translation.MethodDefinitionTranslator;
import org.crashtest.http.translation.Translator;
import org.crashtest.http.validation.Validator;
import org.crashtest.interpreter.model.MethodDef;
import org.crashtest.service.ScopeService;
import org.crashtest.service.impl.SimpleScopeService;
import org.crashtest.service.model.MethodId;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import java.io.InputStream;

public class MethodDefinitionResource extends ServerResource {

    private Deserializer<MethodDefinitionRequest> deserializer = Deserializer.forClass(MethodDefinitionRequest.class);
    private Serializer<MethodDefinitionResponse> serializer = Serializer.forClass(MethodDefinitionResponse.class);
    private Serializer<ErrorResponse> errorSerializer = Serializer.forClass(ErrorResponse.class);
    private Validator validator = Validator.instance();
    private Translator<MethodDefinitionRequest,MethodDef> translator = new MethodDefinitionTranslator();
    ScopeService service = SimpleScopeService.instance();

    @Post("json")
    public String define(InputStream document) throws SerializationException {
        String response;
        try {
            MethodDefinitionRequest request = deserializer.deserialize(document);
            validator.validate(request);
            MethodDef def = translator.translate(request);
            MethodId methodId = service.defineMethod(def);
            MethodDefinitionResponse methodResponse = MethodDefinitionResponse.forId(methodId);
            response = serializer.serialize(methodResponse);
        } catch (Exception e) {
            response = errorSerializer.serialize(ErrorResponse.forException(e));
        }
        return response;
    }

}
