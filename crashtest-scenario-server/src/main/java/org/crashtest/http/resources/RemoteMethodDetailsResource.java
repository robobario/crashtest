package org.crashtest.http.resources;

import org.codehaus.jackson.map.ObjectMapper;
import org.crashtest.service.ScopeService;
import org.crashtest.service.impl.SimpleScopeService;
import org.crashtest.service.model.RemoteMethodId;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class RemoteMethodDetailsResource extends ServerResource{

    ScopeService service = SimpleScopeService.instance();

    private ObjectMapper mapper = new ObjectMapper();

    @Get("json")
    public String getDetails(){
        String id = (String) getRequest().getAttributes().get("remote-method-id");
        try{
            long remoteMethodDetailsId = Long.valueOf(id);
            return mapper.writeValueAsString(service.getRemoteMethodDef(RemoteMethodId.of(remoteMethodDetailsId)));
        }catch (Exception e){
            return "error - could not convert parameter to long :" + id;
        }
    }
}
