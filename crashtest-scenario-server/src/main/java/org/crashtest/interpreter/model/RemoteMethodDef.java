package org.crashtest.interpreter.model;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import org.codehaus.jackson.annotate.JsonAutoDetect;

import java.util.List;

@JsonAutoDetect
public class RemoteMethodDef implements Parameterized {
    private String name;
    private List<ParameterDef> parameters;
    private boolean isAvailable;

    private RemoteMethodDef(String name, List<ParameterDef> parameterDefs, boolean availability) {
        this.name = name;
        this.parameters = parameterDefs;
        this.isAvailable = availability;
    }

    @Override
    public List<ParameterDef> getParameters() {
        return parameters;
    }

    public String getName() {
        return name;
    }

    public static Builder named(String name) {
        return new Builder(name);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("named", name).add("parameters", parameters).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, parameters);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof RemoteMethodDef) {
            final RemoteMethodDef other = (RemoteMethodDef) o;
            return Objects.equal(name, other.name)
                    && Objects.equal(parameters, other.parameters);
        } else {
            return false;
        }
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public static class Builder {
        private String name;
        private ImmutableList.Builder<ParameterDef> parameterDef = ImmutableList.builder();
        private boolean availability;

        private Builder(String name) {
            this.name = name;
        }

        public Builder withParameterDef(ParameterDef def) {
            parameterDef.add(def);
            return this;
        }

        public Builder withParameterDefs(Iterable<ParameterDef> defs) {
            parameterDef.addAll(defs);
            return this;
        }

        public Builder withAvailability(boolean availability) {
            this.availability = availability;
            return this;
        }

        public RemoteMethodDef build() {
            return new RemoteMethodDef(name, parameterDef.build(),availability);
        }
    }

    public RemoteMethodDef withAvailability(boolean availability){
        return named(this.name).withParameterDefs(this.getParameters()).withAvailability(availability).build();
    }
}
