package org.crashtest.model;

import com.google.common.collect.ImmutableList;

import java.util.List;

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

        public Builder withParameterDef(List<ParameterDef> defs){
            parameterDef.addAll(defs);
            return this;
        }

        public RemoteMethodDef build(){
            return new RemoteMethodDef(name, parameterDef.build());
        }


    }
}
