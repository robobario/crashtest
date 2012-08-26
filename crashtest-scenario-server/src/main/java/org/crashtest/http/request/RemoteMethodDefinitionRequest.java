package org.crashtest.http.request;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.List;

public class RemoteMethodDefinitionRequest implements Request{
    private String name;
    private List<ParameterRequest> parameters = ImmutableList.of();

    public String getName() {
        return name;
    }

    public List<ParameterRequest> getParameters() {
        return parameters;
    }

    public void setParameters(List<ParameterRequest> parameters) {
        this.parameters = parameters;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isValid() {
        return !Strings.isNullOrEmpty(name) && Iterables.all(parameters,Requests.IS_VALID);
    }
}
