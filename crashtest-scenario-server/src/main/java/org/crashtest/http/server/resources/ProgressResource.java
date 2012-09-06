package org.crashtest.http.server.resources;

import org.crashtest.http.server.response.ErrorResponse;
import org.crashtest.http.server.response.ExecutionProgressResponse;
import org.crashtest.http.serialization.SerializationException;
import org.crashtest.http.serialization.Serializer;
import org.crashtest.service.ProgressReportProvider;
import org.crashtest.service.ProgressReportService;
import org.crashtest.service.impl.SimpleProgressReportService;
import org.crashtest.service.model.ExecutionId;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class ProgressResource extends ServerResource {
    private static final String LAST_RESORT = "{\"errors\" : [\"failed to serialize error\"]";
    private Serializer<ExecutionProgressResponse> responseSerializer = Serializer.forClass(ExecutionProgressResponse.class);
    private Serializer<ErrorResponse> errorSerializer = Serializer.forClass(ErrorResponse.class);
    private ProgressReportService service = SimpleProgressReportService.instance();

    @Get("json")
    public String getDetails(){
        String id = (String) getRequest().getAttributes().get("execution-id");
        String response;
        try{
            long executionId = Long.valueOf(id);
            ProgressReportProvider progressReport = service.getProviderFor(ExecutionId.of(executionId));
            ExecutionProgressResponse progressResponse = ExecutionProgressResponse.forProgressTree(progressReport.getProgressTree());
            response = responseSerializer.serialize(progressResponse);
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
