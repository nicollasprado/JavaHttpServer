package com.nicollasprado.exceptions;

public class InvalidHttpMethodException extends RuntimeException {
    public InvalidHttpMethodException() {
        super("Invalid HTTP method");
    }
}
