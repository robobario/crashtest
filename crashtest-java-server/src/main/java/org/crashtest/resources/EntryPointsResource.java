package org.crashtest.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.crashtest.request.EntryPointInvocation;
import org.crashtest.response.InvocationResults;
import org.crashtest.service.ClasspathScanningEntryPointService;
import org.crashtest.service.EntryPointInvocationException;
import org.crashtest.service.EntryPointService;
import org.crashtest.service.NonExistentEntryPointException;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import java.io.InputStream;
import java.io.InputStreamReader;

public class EntryPointsResource extends ServerResource {

    private final EntryPointService service = ClasspathScanningEntryPointService.newInstance();
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public EntryPointsResource() {
    }

    @Get("json")
    public String getEntryPoints(){
        return gson.toJson(service.getAllEntryPoints());
    }

    @Post("json")
    public String invoke(InputStream document){
        InvocationResults.Builder builder = InvocationResults.builder();
        EntryPointInvocation entryPointInvocation = gson.fromJson(new InputStreamReader(document), EntryPointInvocation.class);
        try {
            service.invoke(entryPointInvocation);
        } catch (NonExistentEntryPointException e) {
            builder.withErrorMessage("entry point did not exist");
        } catch (EntryPointInvocationException e) {
            builder.withErrorMessage("entry point could not be invoked");
        }
        return gson.toJson(builder.build());
    }
}
