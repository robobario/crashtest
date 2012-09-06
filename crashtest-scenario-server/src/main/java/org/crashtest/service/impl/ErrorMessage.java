package org.crashtest.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.util.List;

public class ErrorMessage {
    private static final Function<String,ErrorMessage> TO_ERROR_MESSAGE = new Function<String, ErrorMessage>() {
        @Override
        public ErrorMessage apply(String input) {
            return ErrorMessage.withMessage(input);
        }
    };

    private String error;

    private ErrorMessage(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public static ErrorMessage withMessage(String errorMessage){
        return new ErrorMessage(errorMessage);
    }

    public static List<ErrorMessage> fromStrings(List<String> errorMessage){
        return Lists.transform(errorMessage, TO_ERROR_MESSAGE);
    }
}
