package com.semenov.gateway.exceptionhandling;

public class GatewayAppException extends RuntimeException {
    public GatewayAppException(String message) {
        super(message);
    }
}
