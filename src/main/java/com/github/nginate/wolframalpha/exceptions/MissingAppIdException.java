package com.github.nginate.wolframalpha.exceptions;

/**
 * Error 2
 */
public class MissingAppIdException extends WolframClientException {
    public MissingAppIdException() {
        super("Missing app id");
    }
}
