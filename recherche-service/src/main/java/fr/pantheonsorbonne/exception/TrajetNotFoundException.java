package fr.pantheonsorbonne.exception;

public class TrajetNotFoundException extends RuntimeException {

    public TrajetNotFoundException(String message) {
        super(message);
    }
}
