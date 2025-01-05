package fr.pantheonsorbonne.exception;

public class ResponseParsingException extends RuntimeException {
    public ResponseParsingException(String message) {
        super(message);
    }

    public ResponseParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
