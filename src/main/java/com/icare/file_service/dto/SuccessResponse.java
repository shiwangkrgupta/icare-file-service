package com.icare.file_service.dto;


public class SuccessResponse {
    private String file;
    private String filePath;
    private int statusCode;
    private String message;
    private boolean success;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


    public SuccessResponse(String fileName, String filePath, int statusCode, String message, boolean success) {
        this.file = fileName;
        this.filePath = filePath;
        this.statusCode = statusCode;
        this.message = message;
        this.success = success;
    }

    public SuccessResponse(String fileName, String filePath, String fileStaticPath, int statusCode, String message, boolean success) {
        this.file = fileName;
        this.filePath = filePath;
        this.statusCode = statusCode;
        this.message = message;
        this.success = success;
    }
}
