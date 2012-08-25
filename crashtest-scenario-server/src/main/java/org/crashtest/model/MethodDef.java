package org.crashtest.model;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class MethodDef {
    private String name;
    private final List<ParameterDef> parameterDefs;
    private List<Statement> statements;

    private MethodDef(String name, List<ParameterDef> parameterDefs, List<Statement> statements){
        this.name = name;
        this.parameterDefs = parameterDefs;
        this.statements = statements;
    }

    public String getName() {
        return name;
    }

    public List<ParameterDef> getParameters() {
        return parameterDefs;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    public static Builder named(String name){
        return new Builder(name);
    }

    public static class Builder{
        String name;
        ImmutableList.Builder<ParameterDef> parameterDefs = ImmutableList.builder();
        ImmutableList.Builder<Statement> statements = ImmutableList.builder();

        private Builder(String name){
            this.name = name;
        }

        public Builder withParameter(ParameterDef def){
             parameterDefs.add(def);
             return this;
        }

        public Builder withParameters(Iterable<ParameterDef> defs){
            parameterDefs.addAll(defs);
            return this;
        }

        public Builder withStatement(Statement statement){
            statements.add(statement);
            return this;
        }

        public Builder withStatements(Iterable<Statement> statements){
            this.statements.addAll(statements);
            return this;
        }

        public MethodDef build(){
            return new MethodDef(name,parameterDefs.build(),statements.build());
        }
    }
}
