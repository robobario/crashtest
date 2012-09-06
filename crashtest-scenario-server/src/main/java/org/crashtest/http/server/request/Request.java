package org.crashtest.http.server.request;

import org.codehaus.jackson.annotate.JsonIgnore;

public interface Request {
    @JsonIgnore
    boolean isValid();
}
