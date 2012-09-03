package org.crashtest.interpreter.model.plan.descriptions;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import org.crashtest.common.model.ParameterDescription;
import org.crashtest.interpreter.model.plan.InvocationStep;
import org.crashtest.interpreter.model.plan.InvocationStepVisitor;

import java.util.List;

public class MethodInvocationStep implements InvocationStep {
    private String name;
    private List<InvocationStep> invocationList;
    private final List<ParameterDescription> parameters;
    private InvocationStep parent;

    private MethodInvocationStep(String name, List<InvocationStep> invocationList, List<ParameterDescription> parameters) {
        this.name = name;
        this.invocationList = invocationList;
        this.parameters = parameters;
    }

    @Override
    public void accept(InvocationStepVisitor visitor) {
        visitor.visit(this);
    }

    public static Builder create(String name) {
        return new Builder(name);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("invocationList", invocationList).add("name",name).add("parameters",parameters).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(invocationList,name,parameters);
    }


    public List<InvocationStep> getInvocationList() {
        return invocationList;
    }


    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof MethodInvocationStep) {
            final MethodInvocationStep other = (MethodInvocationStep) obj;
            return Objects.equal(invocationList, other.invocationList)
                    && Objects.equal(name,other.name)
                    && Objects.equal(parameters,other.parameters);
        } else {
            return false;
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<ParameterDescription> getParameters() {
        return parameters;
    }

    public MethodInvocationStep addChild(InvocationStep method) {
        invocationList = ImmutableList.<InvocationStep>builder().addAll(invocationList).add(method).build();
        return this;
    }

    public InvocationStep getParent() {
        return parent;
    }

    public void setParent(InvocationStep parent) {
        this.parent = parent;
    }

    public static class Builder {
        ImmutableList.Builder<ParameterDescription> parameters = ImmutableList.builder();
        ImmutableList.Builder<InvocationStep> invocationBuilder = ImmutableList.builder();
        private String methodName;

        public Builder(String name) {
            methodName = name;
        }

        public Builder withStep(InvocationStep step) {
            invocationBuilder.add(step);
            return this;
        }

        public Builder withSteps(Iterable<InvocationStep> steps) {
            invocationBuilder.addAll(steps);
            return this;
        }

        public Builder withParameter(ParameterDescription description){
            parameters.add(description);
            return this;
        }
        public Builder withParameters(Iterable<ParameterDescription> descriptions){
            parameters.addAll(descriptions);
            return this;
        }

        public MethodInvocationStep build() {
            return new MethodInvocationStep(methodName, invocationBuilder.build(), parameters.build());
        }
    }
}
