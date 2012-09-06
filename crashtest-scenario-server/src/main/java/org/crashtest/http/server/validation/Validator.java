package org.crashtest.http.server.validation;

import org.crashtest.http.server.request.Request;
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
