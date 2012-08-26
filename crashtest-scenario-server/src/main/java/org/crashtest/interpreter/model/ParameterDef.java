package org.crashtest.interpreter.model;

import com.google.common.base.Objects;

public class ParameterDef {
    private String name;

    public String getName() {
        return name;
    }

    private ParameterDef(String name){
        this.name = name;
    }


    @Override
    public String toString(){
        return Objects.toStringHelper(this).add("name",name).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public boolean equals(final Object obj){
        if(obj instanceof ParameterDef){
            final ParameterDef other = (ParameterDef) obj;
            return Objects.equal(name, other.name);
        } else{
            return false;
        }
    }

    public static ParameterDef named(String name){
        return new ParameterDef(name);
    }
}
