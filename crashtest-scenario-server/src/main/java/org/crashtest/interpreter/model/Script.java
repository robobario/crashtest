package org.crashtest.interpreter.model;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class Script {
    private String name;
    private List<Statement> statements;

    private Script(String name, List<Statement> statements){
        this.name = name;
        this.statements = statements;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    public static Builder named(String name){
        return new Builder(name);
    }

    public String getName() {
        return name;
    }

    public static class Builder {
        private String name;
        private ImmutableList.Builder<Statement> statements = ImmutableList.builder();
        private Builder(String name){
            this.name = name;
        }

        public Builder withStatement(Statement statement){
            statements.add(statement);
            return this;
        }

        public Builder withStatements(Iterable<Statement> statements){
            this.statements.addAll(statements);
            return this;
        }

        public Script build(){
            return new Script(name, statements.build());
        }


    }
}
