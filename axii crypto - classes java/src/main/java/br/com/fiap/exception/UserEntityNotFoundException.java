package br.com.fiap.exception;

public class UserEntityNotFoundException extends EntityNotFoundException {

    public UserEntityNotFoundException() {
    }

    public UserEntityNotFoundException(String message) {
        super(message);
    }
}
