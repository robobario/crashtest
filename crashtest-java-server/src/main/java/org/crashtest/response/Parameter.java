package org.crashtest.response;

import com.google.common.base.Function;
import com.google.common.base.Objects;

public class Parameter {
    public String name;

    private Parameter(String name){
        this.name = name;
    }

    public static Parameter named(String name){
        return new Parameter(name);
    }

    public String toString(){
        return Objects.toStringHelper(this).add("name",name).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public boolean equals(final Object obj){
        if(obj instanceof Parameter){
            final Parameter other = (Parameter) obj;
            return Objects.equal(name, other.name);
        } else{
            return false;
        }
    }

    public String getName() {
        return name;
    }

    public static final Function<Parameter,String> GET_NAME = new Function<Parameter, String>() {
        @Override
        public String apply(Parameter input) {
            return input.getName();
        }
    };
}
