package org.crashtest.http.resources;

import com.google.gson.Gson;
import org.crashtest.service.ScopeService;
import org.crashtest.service.impl.SimpleScopeService;
import org.restlet.resource.ServerResource;

public class RemoteMethodDefinitionResource extends ServerResource{

    private Gson gson = new Gson();

    ScopeService service = SimpleScopeService.instance();


}
