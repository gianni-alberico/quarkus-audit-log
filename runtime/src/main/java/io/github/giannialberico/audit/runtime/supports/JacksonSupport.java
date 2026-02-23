package io.github.giannialberico.audit.runtime.supports;

import io.github.giannialberico.audit.runtime.exceptions.SerializationException;
import io.quarkus.arc.Arc;

public class JacksonSupport {

    public static String serialize(Object obj) {
        try {
            return getMapper().writeValueAsString(obj);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new SerializationException("Jackson serialization failed", e);
        }
    }

    private static com.fasterxml.jackson.databind.ObjectMapper getMapper() {
        return Arc.container().instance(com.fasterxml.jackson.databind.ObjectMapper.class).get();
    }
}