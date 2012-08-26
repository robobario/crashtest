package org.crashtest.http.translation;

public interface Translator<T,Y> {
    public Y translate(T toTranslate) throws TranslationException;
}
