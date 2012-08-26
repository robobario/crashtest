package org.crashtest.http.resources;

import org.crashtest.http.response.ErrorResponse;
import org.crashtest.http.response.ScriptDetailsResponse;
import org.crashtest.http.serialization.SerializationException;
import org.crashtest.http.serialization.Serializer;
import org.crashtest.interpreter.model.Script;
import org.crashtest.service.ScriptRepositoryService;
import org.crashtest.service.impl.SimpleScriptRepositoryService;
import org.crashtest.service.model.ScriptId;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class ScriptDetailsResource extends ServerResource {
    ScriptRepositoryService service = SimpleScriptRepositoryService.instance();

    private static final String LAST_RESORT = "{\"errors\" : [\"failed to serialize error\"]";
    private Serializer<ScriptDetailsResponse> serializer = Serializer.forClass(ScriptDetailsResponse.class);
    private Serializer<ErrorResponse> errorSerializer = Serializer.forClass(ErrorResponse.class);


    @Get("json")
    public String getDetails() {
        String id = (String) getRequest().getAttributes().get("script-id");
        String response;
        try {
            long scriptId = Long.valueOf(id);
            Script script = service.getScript(ScriptId.of(scriptId));
            response = serializer.serialize(ScriptDetailsResponse.responseFor(script));
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
