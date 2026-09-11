package br.com.fiap.exception;

public class UserEntityNotFoundException extends Exception {
    public UserEntityNotFoundException() {
    }

    public UserEntityNotFoundException(String message) {
        super(message);
    }
}
