package org.crashtest.http.resources;

import org.crashtest.http.response.ErrorResponse;
import org.crashtest.http.response.InvocationSuccessResponse;
import org.crashtest.http.serialization.SerializationException;
import org.crashtest.http.serialization.Serializer;
import org.crashtest.service.ScriptExecutorService;
import org.crashtest.service.impl.SimpleScriptExecutorService;
import org.crashtest.service.model.ExecutionId;
import org.crashtest.service.model.ScriptId;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class ScriptInvocationResource extends ServerResource {

    ScriptExecutorService service = SimpleScriptExecutorService.getInstance();
    private static final String LAST_RESORT = "{\"errors\" : [\"failed to serialize error\"]";
    private Serializer<InvocationSuccessResponse> serializer = Serializer.forClass(InvocationSuccessResponse.class);
    private Serializer<ErrorResponse> errorSerializer = Serializer.forClass(ErrorResponse.class);

    @Get("json")
    public String execute() {
        String id = (String) getRequest().getAttributes().get("script-id");
        String response;
        try {
            long scriptId = Long.valueOf(id);
            ExecutionId executionId = service.execute(ScriptId.of(scriptId));
            response = serializer.serialize(InvocationSuccessResponse.instance(executionId));
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
