package org.crashtest.http.request;

import org.codehaus.jackson.annotate.JsonIgnore;

public interface Request {
    @JsonIgnore
    boolean isValid();
}
