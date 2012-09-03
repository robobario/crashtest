package org.crashtest.interpreter.model.plan.descriptions;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import org.crashtest.common.model.ParameterDescription;
import org.crashtest.interpreter.model.plan.InvocationStep;
import org.crashtest.interpreter.model.plan.InvocationStepVisitor;

import java.util.List;

public class ScriptInvocationStep implements InvocationStep{
    private String name;
    public List<InvocationStep> invocationList;

    private ScriptInvocationStep(String name, List<InvocationStep> invocationList) {
        this.name = name;
        this.invocationList = invocationList;
    }

    @Override
    public void accept(InvocationStepVisitor visitor) {
        visitor.visit(this);
    }

    public static Builder named(String name) {
        return new Builder(name);
    }

    public String toString() {
        return Objects.toStringHelper(this).add("invocationList", invocationList).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(invocationList,name);
    }


    public List<InvocationStep> getInvocationList() {
        return invocationList;
    }


    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ScriptInvocationStep) {
            final ScriptInvocationStep other = (ScriptInvocationStep) obj;
            return Objects.equal(invocationList, other.invocationList)
                    && Objects.equal(name,other.name);
        } else {
            return false;
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public List<ParameterDescription> getParameters() {
        return ImmutableList.of();
    }

    public void addChild(InvocationStep step) {
        invocationList = ImmutableList.<InvocationStep>builder().addAll(invocationList).add(step).build();
    }

    public static class Builder {
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

        public ScriptInvocationStep build() {
            return new ScriptInvocationStep(methodName, invocationBuilder.build());
        }
    }
}
