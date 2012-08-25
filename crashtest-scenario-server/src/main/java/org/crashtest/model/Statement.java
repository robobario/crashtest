package org.crashtest.model;

public interface Statement {
    public void accept(StatementVisitor visitor);
}
