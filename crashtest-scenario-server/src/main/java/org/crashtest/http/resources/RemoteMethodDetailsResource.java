package org.crashtest.http.resources;

import org.crashtest.http.response.ErrorResponse;
import org.crashtest.http.response.RemoteMethodDetailsResponse;
import org.crashtest.http.serialization.SerializationException;
import org.crashtest.http.serialization.Serializer;
import org.crashtest.interpreter.model.RemoteMethodDef;
import org.crashtest.service.ScopeService;
import org.crashtest.service.impl.SimpleScopeService;
import org.crashtest.service.model.RemoteMethodId;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class RemoteMethodDetailsResource extends ServerResource {
    ScopeService service = SimpleScopeService.instance();
    private static final String LAST_RESORT = "{\"errors\" : [\"failed to serialize error\"]";
    private Serializer<RemoteMethodDetailsResponse> serializer = Serializer.forClass(RemoteMethodDetailsResponse.class);
    private Serializer<ErrorResponse> errorSerializer = Serializer.forClass(ErrorResponse.class);

    @Get("json")
    public String getDetails() {
        String id = (String) getRequest().getAttributes().get("remote-method-id");
        String response;
        try {
            long remoteMethodDetailsId = Long.valueOf(id);
            RemoteMethodDef remoteMethodDef = service.getRemoteMethodDef(RemoteMethodId.of(remoteMethodDetailsId));
            response = serializer.serialize(RemoteMethodDetailsResponse.responseFor(remoteMethodDef));
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
