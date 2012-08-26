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

    public static ParameterDef named(String name){
        return new ParameterDef(name);
    }
}
