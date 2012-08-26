package org.crashtest.http.translation;

public class TranslationException extends Exception {

    public TranslationException(String s) {
        super(s);
    }
    public TranslationException(Exception e) {
        super(e);
    }
}
