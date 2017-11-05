package com.github.nginate.wolframalpha.exceptions;

public class WolframClientException extends RuntimeException {
    public WolframClientException() {
    }

    public WolframClientException(String message) {
        super(message);
    }
}
