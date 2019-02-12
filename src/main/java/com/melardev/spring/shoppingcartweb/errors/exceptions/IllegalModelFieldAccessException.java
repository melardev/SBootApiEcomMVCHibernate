package com.melardev.spring.shoppingcartweb.errors.exceptions;

public class IllegalModelFieldAccessException extends RuntimeException {

    public IllegalModelFieldAccessException() {
        super("You are trying to access a field which is not supposed to be accessible and hence not loaded from the database." +
                "To fix this problem override the accessor you are interested in on your extension model");
    }
}
