package org.crashtest.http.server.translation;

public interface Translator<T,Y> {
    public Y translate(T toTranslate) throws TranslationException;
}
