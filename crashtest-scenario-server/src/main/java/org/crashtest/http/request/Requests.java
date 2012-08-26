package org.crashtest.http.request;

import com.google.common.base.Predicate;

public class Requests {
    public static final Predicate<Request> IS_VALID = new Predicate<Request>() {
        @Override
        public boolean apply(Request input) {
            return input.isValid();
        }
    };
}
