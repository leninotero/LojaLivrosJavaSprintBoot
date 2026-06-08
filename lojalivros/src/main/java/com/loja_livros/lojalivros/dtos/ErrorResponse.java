package com.loja_livros.lojalivros.dtos;

import java.time.LocalDateTime;

public class ErrorResponse {
    private int status;
    private String message;
    private String detail;
    private LocalDateTime timestamp;
    private String path;

    public ErrorResponse() {
    }

    public ErrorResponse(int status, String message, String detail, LocalDateTime timestamp, String path) {
        this.status = status;
        this.message = message;
        this.detail = detail;
        this.timestamp = timestamp;
        this.path = path;
    }

    public ErrorResponse(int status, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

