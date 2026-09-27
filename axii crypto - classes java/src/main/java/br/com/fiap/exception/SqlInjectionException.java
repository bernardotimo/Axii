package br.com.fiap.exception;

public class SqlInjectionException extends RuntimeException {

    public SqlInjectionException(String message) {
        super(message);
    }
}
