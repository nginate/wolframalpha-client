package com.github.nginate.wolframalpha.exceptions;

/**
 * Error 1
 */
public class InvalidAppIdException extends WolframClientException {
    public InvalidAppIdException() {
        super("Invalid app id");
    }
}
