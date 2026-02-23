package io.github.giannialberico.audit.runtime.exceptions;

public class SerializationException extends RuntimeException {
    public SerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}