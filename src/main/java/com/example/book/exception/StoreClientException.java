package com.example.book.exception;

import org.springframework.http.HttpStatus;

public class StoreClientException extends RuntimeException {
    private HttpStatus statusCode;
    private String error;

    public StoreClientException(HttpStatus statusCode, String error) {
        super(error);
        this.statusCode = statusCode;
        this.error = error;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "StoreClientException{" +
                "statusCode=" + statusCode +
                ", error='" + error + '\'' +
                '}';
    }
}
