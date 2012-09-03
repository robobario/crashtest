package org.crashtest.interpreter;

public class ScriptExecutionException extends Exception {
    public ScriptExecutionException(Exception e) {
        super(e);
    }

    public ScriptExecutionException(String s) {
        super(s);
    }
}
