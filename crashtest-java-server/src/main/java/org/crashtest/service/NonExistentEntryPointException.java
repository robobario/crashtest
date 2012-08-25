package org.crashtest.service;

public class NonExistentEntryPointException extends Exception {
    public NonExistentEntryPointException(String entryPointName) {
        super(entryPointName);
    }
}
