package org.crashtest.model;

public class ParameterDef {
    private String name;

    public String getName() {
        return name;
    }

    private ParameterDef(String name){
        this.name = name;
    }

    public static ParameterDef named(String name){
        return new ParameterDef(name);
    }
}
