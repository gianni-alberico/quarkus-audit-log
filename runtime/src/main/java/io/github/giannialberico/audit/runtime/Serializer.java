package io.github.giannialberico.audit.runtime;

import io.github.giannialberico.audit.runtime.supports.JacksonSupport;
import io.github.giannialberico.audit.runtime.supports.JsonbSupport;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Serializer {

    private static final boolean JACKSON_PRESENT =
            isPresent("com.fasterxml.jackson.databind.ObjectMapper");
    private static final boolean JSONB_PRESENT =
            isPresent("jakarta.json.bind.Jsonb");

    private static boolean isPresent(String className) {
        try {
            Class.forName(className, false,
                    Thread.currentThread().getContextClassLoader());
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public String serialize(Object entity) {
        if (JACKSON_PRESENT) {
            return JacksonSupport.serialize(entity);
        }
        if (JSONB_PRESENT) {
            return JsonbSupport.serialize(entity);
        }
        return entity.toString();
    }
}
