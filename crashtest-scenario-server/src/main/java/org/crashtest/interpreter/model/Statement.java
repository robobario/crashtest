package org.crashtest.interpreter.model;

public interface Statement {
    public void accept(StatementVisitor visitor);
}
