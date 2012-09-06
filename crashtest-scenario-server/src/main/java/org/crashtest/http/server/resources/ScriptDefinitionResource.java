package org.crashtest.http.server.resources;

import org.crashtest.http.server.request.ScriptDefinitionRequest;
import org.crashtest.http.server.response.ErrorResponse;
import org.crashtest.http.server.response.ScriptDefinitionResponse;
import org.crashtest.http.serialization.Deserializer;
import org.crashtest.http.serialization.SerializationException;
import org.crashtest.http.serialization.Serializer;
import org.crashtest.http.server.translation.ScriptDefinitionTranslator;
import org.crashtest.http.server.translation.Translator;
import org.crashtest.http.server.validation.Validator;
import org.crashtest.interpreter.model.Script;
import org.crashtest.service.ScriptRepositoryService;
import org.crashtest.service.impl.SimpleScriptRepositoryService;
import org.crashtest.service.model.ScriptId;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import java.io.InputStream;

public class ScriptDefinitionResource extends ServerResource{
    private static final String LAST_RESORT = "{\"errors\" : [\"failed to serialize error\"]";
    private Deserializer<ScriptDefinitionRequest> deserializer = Deserializer.forClass(ScriptDefinitionRequest.class);
    private ScriptRepositoryService service = SimpleScriptRepositoryService.instance();
    private Validator validator = Validator.instance();
    private Translator<ScriptDefinitionRequest,Script> translator = new ScriptDefinitionTranslator();
    private Serializer<ScriptDefinitionResponse> serializer = Serializer.forClass(ScriptDefinitionResponse.class);
    private Serializer<ErrorResponse> errorSerializer = Serializer.forClass(ErrorResponse.class);

    @Post("json")
    public String define(InputStream document){
        String response;
        try{
            ScriptDefinitionRequest request = deserializer.deserialize(document);
            validator.validate(request);
            Script script = translator.translate(request);
            ScriptId scriptId = service.addScript(script);
            response = serializer.serialize(ScriptDefinitionResponse.forId(scriptId));
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
