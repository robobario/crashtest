package org.crashtest.http.serialization;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Deserializer<Y> {
    private Class<Y> clazz;
    private ObjectMapper mapper = new ObjectMapper();

    private Deserializer(Class<Y> clazz) {
        this.clazz = clazz;
    }

    public Y deserialize(InputStream stream) throws SerializationException {
        try {
            return mapper.readValue(new InputStreamReader(stream),clazz);
        } catch (IOException e) {
            throw new SerializationException("deserialization failed",e);
        }
    }

    public static <E> Deserializer<E> forClass(Class<E> clazz){
        return new Deserializer<E>(clazz);
    }
}
