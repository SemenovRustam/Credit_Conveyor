package com.semenov.creditconveyor.msconveyor.exceptionhandling;

public class ValidationException extends  RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
