package org.crashtest.http.server.translation;

public class TranslationException extends Exception {

    public TranslationException(String s) {
        super(s);
    }
    public TranslationException(Exception e) {
        super(e);
    }
}
