package org.crashtest.http.client.request;

import com.google.common.collect.ImmutableList;
import org.crashtest.common.model.ParameterDescription;
import org.crashtest.service.model.RemoteInvocationDescription;

import java.util.List;

public class RemoteMethodInvocation {
    private String entryPointName;
    private List<ParameterDescription> parameters = ImmutableList.of();
    public RemoteMethodInvocation(){}

    public void setEntryPointName(String entryPointName) {
        this.entryPointName = entryPointName;
    }

    public void setParameters(List<ParameterDescription> parameters) {
        this.parameters = parameters;
    }

    public String getEntryPointName() {
        return entryPointName;
    }

    public List<ParameterDescription> getParameters() {
        return parameters;
    }

    public static RemoteMethodInvocation transform(RemoteInvocationDescription description) {
        RemoteMethodInvocation remoteMethodInvocation = new RemoteMethodInvocation();
        remoteMethodInvocation.setEntryPointName(description.getEntryPointName());
        remoteMethodInvocation.setParameters(description.getParameters());
        return remoteMethodInvocation;
    }
}
