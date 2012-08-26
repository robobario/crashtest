package org.crashtest.service.model;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

import java.util.List;

public class RemoteInvocationDescription {
    private String entryPointName;
    private List<ParameterDescription> parameters;
    private RemoteInvocationDescription(String entryPointName, ImmutableList<ParameterDescription> parameterDescriptions) {
        this.entryPointName = entryPointName;
        this.parameters = parameterDescriptions;
    }

    public static Builder named(String name){
        return new Builder(name);
    }

    public String toString(){
        return Objects.toStringHelper(this).add("entryPointName",entryPointName).add("parameters",parameters).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(entryPointName,parameters);
    }

    @Override
    public boolean equals(final Object obj){
        if(obj instanceof RemoteInvocationDescription){
            final RemoteInvocationDescription other = (RemoteInvocationDescription) obj;
            return Objects.equal(entryPointName, other.entryPointName)
                    && Objects.equal(parameters,other.parameters);
        } else{
            return false;
        }
    }

    public static class Builder {
        private String entryPointName;
        ImmutableList.Builder<ParameterDescription> invocationBuilder = ImmutableList.builder();
        public Builder(String name){
            entryPointName = name;
        }
        public Builder withParameter(ParameterDescription description){
            invocationBuilder.add(description);
            return this;
        }
        public Builder withParameters(Iterable<ParameterDescription> descriptions){
            invocationBuilder.addAll(descriptions);
            return this;
        }
        public RemoteInvocationDescription build(){
            return new RemoteInvocationDescription(entryPointName,invocationBuilder.build());
        }
    }
}
