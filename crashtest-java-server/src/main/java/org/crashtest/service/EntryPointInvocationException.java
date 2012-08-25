package org.crashtest.service;

public class EntryPointInvocationException extends Exception {
    public EntryPointInvocationException(Exception e) {
        super(e);
    }

    public EntryPointInvocationException(String message) {
        super(message);
    }
}
