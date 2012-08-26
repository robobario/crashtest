package org.crashtest.http.request;

public interface StatementRequest {
    public void accept(StatementRequestVisitor visitor);
}
