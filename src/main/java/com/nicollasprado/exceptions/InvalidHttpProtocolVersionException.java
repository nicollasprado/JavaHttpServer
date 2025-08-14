package com.nicollasprado.exceptions;

public class InvalidHttpProtocolVersionException extends RuntimeException {
    public InvalidHttpProtocolVersionException() {
        super("Invalid HTTP protocol version");
    }
}
