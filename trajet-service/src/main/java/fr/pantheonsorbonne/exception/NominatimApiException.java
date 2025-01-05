package fr.pantheonsorbonne.exception;

public class NominatimApiException extends RuntimeException {
    public NominatimApiException(String message) {
        super(message);
    }

    public NominatimApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
