package com.rustam_semenov.credit_conveyor.ms_conveyor.exception_handling;

public class ValidationException extends  RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
