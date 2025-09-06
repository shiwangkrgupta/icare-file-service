package com.icare.file_service.dto;

public class ErrorResponse {
    private int statusCode;
    private String errorMessage;
    private boolean success;

    public ErrorResponse(int statusCode, String errorMessage, boolean success) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        this.success = success;
    }

    public ErrorResponse() {
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

