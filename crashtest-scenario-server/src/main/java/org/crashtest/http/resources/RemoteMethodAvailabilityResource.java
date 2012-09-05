package org.crashtest.http.resources;

import org.crashtest.http.response.ErrorResponse;
import org.crashtest.http.serialization.SerializationException;
import org.crashtest.http.serialization.Serializer;
import org.crashtest.service.RemoteMethodAvailabilityService;
import org.crashtest.service.impl.SingleServerRemoteMethodAvailabilityService;
import org.crashtest.service.model.RemoteMethodId;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class RemoteMethodAvailabilityResource extends ServerResource {

    private RemoteMethodAvailabilityService service = SingleServerRemoteMethodAvailabilityService.instance();
    private static final String LAST_RESORT = "{\"errors\" : [\"failed to serialize error\"]";
    private Serializer<ErrorResponse> errorSerializer = Serializer.forClass(ErrorResponse.class);

    @Get("json")
    public String checkAvailability() {
        String id = (String) getRequest().getAttributes().get("remote-method-id");
        String response;
        try {
            long remoteMethodDetailsId = Long.valueOf(id);
            Boolean isAvailable = service.isRemoteMethodAvailable(RemoteMethodId.of(remoteMethodDetailsId));
            response = isAvailable.toString();
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
