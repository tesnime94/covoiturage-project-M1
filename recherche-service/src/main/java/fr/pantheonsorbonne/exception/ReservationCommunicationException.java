package fr.pantheonsorbonne.exception;


public class ReservationCommunicationException extends RuntimeException {

    public ReservationCommunicationException(String message) {
        super(message);
    }

    public ReservationCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}