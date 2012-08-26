package org.crashtest.http.serialization;


import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class Serializer<Y> {
    private Class<Y> clazz;
    private ObjectMapper mapper = new ObjectMapper();

    private Serializer(Class<Y> clazz) {
        this.clazz = clazz;
    }

    public String serialize(Y object) throws SerializationException {
        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            throw new SerializationException("serialization failed",e);
        }
    }

    public static <E> Serializer<E> forClass(Class<E> clazz){
        return new Serializer<E>(clazz);
    }
}
