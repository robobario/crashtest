package org.crashtest.http.resources;

import org.codehaus.jackson.map.ObjectMapper;
import org.crashtest.service.NoSuchScriptDefinedException;
import org.crashtest.service.ScriptRepositoryService;
import org.crashtest.service.impl.SimpleScriptRepositoryService;
import org.crashtest.service.model.ScriptId;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class ScriptDetailsResource extends ServerResource {
    ScriptRepositoryService service = SimpleScriptRepositoryService.instance();

    private ObjectMapper mapper = new ObjectMapper();


    @Get("json")
    public String getDetails(){
        String id = (String) getRequest().getAttributes().get("script-id");
        try{
            long scriptId = Long.valueOf(id);
            return mapper.writeValueAsString(service.getScript(ScriptId.of(scriptId)));
        }catch (NumberFormatException e){
            return "error - could not convert parameter to long :" + id;
        }catch (NoSuchScriptDefinedException e){
            return "error - no script defined with id :" + id;
        } catch (Exception e) {
            return "error :" + e.toString();
        }
    }
}
