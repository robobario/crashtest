package org.crashtest.http.response;

import com.google.common.collect.ImmutableList;
import org.crashtest.service.model.ExecutionId;

import java.util.List;

public class InvocationSuccessResponse implements Response {
    private ExecutionId executionId;

    private InvocationSuccessResponse(ExecutionId executionId) {
        this.executionId = executionId;
    }

    public String getProgressUrl(){
        return "/crashtest/progress/" + executionId;
    }

    @Override
    public List<String> getErrors() {
        return ImmutableList.of();
    }


    public static InvocationSuccessResponse instance(ExecutionId executionId){
        return new InvocationSuccessResponse(executionId);
    }
}
