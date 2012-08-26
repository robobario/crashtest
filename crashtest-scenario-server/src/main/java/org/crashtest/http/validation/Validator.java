package org.crashtest.http.validation;

import org.crashtest.http.request.Request;
public class Validator {

    private Validator(){

    }

    public void validate(Request request) throws ValidationException {
        if(!request.isValid()){
            throw new ValidationException("request was invalid");
        }
    }

    public static Validator instance() {
        return new Validator();
    }
}
