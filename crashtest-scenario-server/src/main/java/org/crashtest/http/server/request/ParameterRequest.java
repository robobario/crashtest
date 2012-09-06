package org.crashtest.http.server.request;

import com.google.common.base.Strings;

public class ParameterRequest implements Request {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isValid() {
        return !Strings.isNullOrEmpty(name);
    }
}
