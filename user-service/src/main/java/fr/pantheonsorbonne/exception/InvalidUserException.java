package fr.pantheonsorbonne.exception;

public class InvalidUserException extends Throwable {
    public InvalidUserException(String emailIsMalformed) {
        super(emailIsMalformed);
    }
}
