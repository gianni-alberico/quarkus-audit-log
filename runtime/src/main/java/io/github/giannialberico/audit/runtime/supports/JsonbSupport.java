package io.github.giannialberico.audit.runtime.supports;

import io.github.giannialberico.audit.runtime.exceptions.SerializationException;
import io.quarkus.arc.Arc;

public class JsonbSupport {

    public static String serialize(Object obj) {
        try {
            return getJsonb().toJson(obj);
        } catch (jakarta.json.bind.JsonbException e) {
            throw new SerializationException("JsonB serialization failed", e);
        }
    }

    private static jakarta.json.bind.Jsonb getJsonb() {
        return Arc.container().instance(jakarta.json.bind.Jsonb.class).get();
    }
}
