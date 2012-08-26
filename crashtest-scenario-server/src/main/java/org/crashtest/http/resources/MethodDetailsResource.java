package org.crashtest.http.resources;

import org.crashtest.http.response.ErrorResponse;
import org.crashtest.http.response.MethodDetailsResponse;
import org.crashtest.http.serialization.SerializationException;
import org.crashtest.http.serialization.Serializer;
import org.crashtest.interpreter.model.MethodDef;
import org.crashtest.service.ScopeService;
import org.crashtest.service.impl.SimpleScopeService;
import org.crashtest.service.model.MethodId;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class MethodDetailsResource extends ServerResource{

    ScopeService service = SimpleScopeService.instance();
    private static final String LAST_RESORT = "{\"errors\" : [\"failed to serialize error\"]";
    private Serializer<MethodDetailsResponse> responseSerializer = Serializer.forClass(MethodDetailsResponse.class);
    private Serializer<ErrorResponse> errorSerializer = Serializer.forClass(ErrorResponse.class);

    @Get("json")
    public String getDetails(){
        String id = (String) getRequest().getAttributes().get("method-id");
        String response;
        try{
            long methodDetailsId = Long.valueOf(id);
            MethodDef methodDef = service.getMethodDef(MethodId.of(methodDetailsId));
            MethodDetailsResponse detailsResponse = MethodDetailsResponse.responseFor(methodDef);
            response = responseSerializer.serialize(detailsResponse);
        }catch (Exception e){
            try {
                response =  errorSerializer.serialize(ErrorResponse.forException(e));
            } catch (SerializationException e1) {
                response = LAST_RESORT;
            }
        }
        return response;
    }
}
