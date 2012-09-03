package org.crashtest.interpreter.model.plan.descriptions;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import org.crashtest.common.model.ParameterDescription;
import org.crashtest.interpreter.model.plan.InvocationStep;
import org.crashtest.interpreter.model.plan.InvocationStepVisitor;

import java.util.List;

public class RemoteMethodInvocationStep implements InvocationStep {
    private final String name;
    private final List<ParameterDescription> parameters;
    private InvocationStep parent;

    private RemoteMethodInvocationStep(String entryPointName, ImmutableList<ParameterDescription> parameterDescriptions) {
        this.name = entryPointName;
        this.parameters = parameterDescriptions;
    }

    public static Builder named(String name){
        return new Builder(name);
    }

    public String toString(){
        return Objects.toStringHelper(this).add("name", name).add("parameters",parameters).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name,parameters);
    }

    public String getName() {
        return name;
    }

    public List<ParameterDescription> getParameters() {
        return parameters;
    }


    @Override
    public boolean equals(final Object obj){
        if(obj instanceof RemoteMethodInvocationStep){
            final RemoteMethodInvocationStep other = (RemoteMethodInvocationStep) obj;
            return Objects.equal(name, other.name)
                    && Objects.equal(parameters,other.parameters);
        } else{
            return false;
        }
    }

    public InvocationStep getParent() {
        return parent;
    }

    public void setParent(InvocationStep parent) {
        this.parent = parent;
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
        public RemoteMethodInvocationStep build(){
            return new RemoteMethodInvocationStep(entryPointName,invocationBuilder.build());
        }
    }
    @Override
    public void accept(InvocationStepVisitor visitor) {
       visitor.visit(this);
    }
}
