package org.crashtest.http.server.resources;

import org.crashtest.http.server.response.ErrorResponse;
import org.crashtest.http.server.response.ScriptListResponse;
import org.crashtest.http.serialization.SerializationException;
import org.crashtest.http.serialization.Serializer;
import org.crashtest.service.ScriptRepositoryService;
import org.crashtest.service.impl.SimpleScriptRepositoryService;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class ScriptListResource extends ServerResource{
    private static final String LAST_RESORT = "{\"errors\" : [\"failed to serialize error\"]";
    private Serializer<ScriptListResponse> listSerializer = Serializer.forClass(ScriptListResponse.class);
    private Serializer<ErrorResponse> errorSerializer = Serializer.forClass(ErrorResponse.class);
    private ScriptRepositoryService service = SimpleScriptRepositoryService.instance();

    @Get("json")
    public String getAll() {
        String response;
        try {
            response = listSerializer.serialize(ScriptListResponse.ofScriptIds(service.getAllScriptIds()));
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
