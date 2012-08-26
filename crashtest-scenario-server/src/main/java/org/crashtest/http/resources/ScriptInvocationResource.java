package org.crashtest.http.resources;

import org.codehaus.jackson.map.ObjectMapper;
import org.crashtest.interpreter.ScriptExecutionException;
import org.crashtest.service.ScriptExecutorService;
import org.crashtest.service.impl.SimpleScriptExecutorService;
import org.crashtest.service.model.ScriptId;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class ScriptInvocationResource extends ServerResource{

    ScriptExecutorService service = SimpleScriptExecutorService.getInstance();

    private ObjectMapper mapper = new ObjectMapper();

    @Get("json")
    public String execute(){
        String id = (String) getRequest().getAttributes().get("script-id");
        try{
            long scriptId = Long.valueOf(id);
            service.execute(ScriptId.of(scriptId));
        }catch (ScriptExecutionException e){
            return "error - script execution went horribly wrong: " + e;
        }
        return "Success";
    }
}
