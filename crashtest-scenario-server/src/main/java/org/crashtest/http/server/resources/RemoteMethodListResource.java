package org.crashtest.http.server.resources;

import org.crashtest.http.server.response.ErrorResponse;
import org.crashtest.http.server.response.RemoteMethodDetailsListResponse;
import org.crashtest.http.serialization.SerializationException;
import org.crashtest.http.serialization.Serializer;
import org.crashtest.service.ScopeService;
import org.crashtest.service.impl.SimpleScopeService;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class RemoteMethodListResource extends ServerResource {
    private static final String LAST_RESORT = "{\"errors\" : [\"failed to serialize error\"]";
    private Serializer<RemoteMethodDetailsListResponse> listSerializer = Serializer.forClass(RemoteMethodDetailsListResponse.class);
    private Serializer<ErrorResponse> errorSerializer = Serializer.forClass(ErrorResponse.class);
    private ScopeService service = SimpleScopeService.instance();

    @Get("json")
    public String getAll() {
        String response;
        try {
            response = listSerializer.serialize(RemoteMethodDetailsListResponse.forDefinitionIds(service.getAllRemoteMethodIds()));
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
