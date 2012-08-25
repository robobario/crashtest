package org.crashtest.response;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;

public class EntryPoint {
    public String getName() {
        return name;
    }

    public List<String> getParameterNames(){
        return Lists.transform(parameters,Parameter.GET_NAME);
    }

    private String name;
    private List<Parameter> parameters;

    private EntryPoint(String name, List<Parameter> parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public static Builder named(String name){
        return new Builder(name);
    }

    public String toString(){
        return Objects.toStringHelper(this).add("name",name).add("parameters",parameters).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name,parameters);
    }

    @Override
    public boolean equals(final Object obj){
        if(obj instanceof EntryPoint){
            final EntryPoint other = (EntryPoint) obj;
            return Objects.equal(name, other.name)
                    && Objects.equal(parameters,other.parameters);
        } else{
            return false;
        }
    }

    public static class Builder {
        private String name;
        private ImmutableList.Builder<Parameter> parameters;

        private Builder(String name){
            this.name = name;
            parameters = ImmutableList.builder();
        }

        public Builder withParameter(Parameter parameter){
            parameters.add(parameter);
            return this;
        }

        public Builder withParameters(Iterable<Parameter> params){
            parameters.addAll(params);
            return this;
        }

        public EntryPoint build(){
            return new EntryPoint(name,parameters.build());
        }
    }

    public static final Function<EntryPoint,String> GET_NAME = new Function<EntryPoint, String>() {
        @Override
        public String apply(EntryPoint input) {
            return input.getName();
        }
    };

}
