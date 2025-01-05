package fr.pantheonsorbonne.exception;

public class OverpassApiException extends RuntimeException {
    public OverpassApiException(String message) {
        super(message);
    }

    public OverpassApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
