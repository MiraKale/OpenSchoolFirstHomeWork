package com.example.supplierservice.error_handler;

public class RestTemplateException extends RuntimeException {
    final String message;

    public RestTemplateException(String message) {
        this.message = message;
    }
}

