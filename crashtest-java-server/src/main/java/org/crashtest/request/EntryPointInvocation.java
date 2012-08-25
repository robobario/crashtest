package org.crashtest.request;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;

import java.util.List;

public class EntryPointInvocation {
    private String entryPointName;
    private List<ParameterInvocation> parameters = ImmutableList.of();
    public EntryPointInvocation(){}

    public void setEntryPointName(String entryPointName) {
        this.entryPointName = entryPointName;
    }

    public void setParameters(List<ParameterInvocation> parameters) {
        this.parameters = parameters;
    }

    public String getEntryPointName() {
        return entryPointName;
    }

    public List<ParameterInvocation> getParameters() {
        return parameters;
    }

    public static final Function<EntryPointInvocation,String> GET_NAME = new Function<EntryPointInvocation, String>() {
        @Override
        public String apply(EntryPointInvocation input) {
            return input.getEntryPointName();
        }
    };
}
