package org.crashtest.http.response;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class ErrorResponse implements Response{
    private List<String> errors;

    private ErrorResponse(List<String> errors) {
        this.errors = errors;
    }

    @Override
    public List<String> getErrors() {
        return errors;
    }

    public static ErrorResponse forException(Exception e){
        return new ErrorResponse(ImmutableList.of(e.toString()));
    }
}