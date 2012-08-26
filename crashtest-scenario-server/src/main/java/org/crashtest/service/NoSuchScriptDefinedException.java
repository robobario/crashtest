package org.crashtest.service;

public class NoSuchScriptDefinedException extends Exception {
    public NoSuchScriptDefinedException(String s) {
        super(s);
    }
}
