package com.example.supplierservice.exception;

public class IdAlreadyExistException extends RuntimeException{
    final String message;

    public IdAlreadyExistException(String message) {
        this.message = message;
    }
}
