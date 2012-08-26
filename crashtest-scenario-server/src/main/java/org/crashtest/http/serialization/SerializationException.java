package org.crashtest.http.serialization;

import java.io.IOException;

public class SerializationException extends Exception {
    public SerializationException(String s, IOException e) {
        super(s,e);
    }
}
