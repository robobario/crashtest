package org.crashtest.http.response;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class SuccessResponse implements Response {
    private SuccessResponse() {
    }

    @Override
    public List<String> getErrors() {
        return ImmutableList.of();
    }


    public static SuccessResponse instance(){
        return new SuccessResponse();
    }
}
