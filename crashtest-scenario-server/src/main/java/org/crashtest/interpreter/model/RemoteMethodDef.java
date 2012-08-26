package org.crashtest.interpreter.model;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import org.codehaus.jackson.annotate.JsonAutoDetect;

import java.util.List;

@JsonAutoDetect
public class RemoteMethodDef {
    private String name;
    private List<ParameterDef> parameters;

    private RemoteMethodDef(String name, List<ParameterDef> parameterDefs){
        this.name = name;
        this.parameters = parameterDefs;
    }

    public List<ParameterDef> getParameters() {
        return parameters;
    }

    public String getName() {
        return name;
    }

    public static Builder named(String name){
        return new Builder(name);
    }

    @Override
    public String toString(){
        return Objects.toStringHelper(this).add("name",name).add("parameters",parameters).toString();
    }

    public static class Builder {
        private String name;
        private ImmutableList.Builder<ParameterDef> parameterDef = ImmutableList.builder();
        private Builder(String name){
            this.name = name;
        }

        public Builder withParameterDef(ParameterDef def){
            parameterDef.add(def);
            return this;
        }

        public Builder withParameterDefs(Iterable<ParameterDef> defs){
            parameterDef.addAll(defs);
            return this;
        }

        public RemoteMethodDef build(){
            return new RemoteMethodDef(name, parameterDef.build());
        }


    }
}
