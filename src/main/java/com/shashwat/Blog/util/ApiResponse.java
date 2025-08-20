package com.shashwat.Blog.util;

public class ApiResponse {
    public boolean status;
    public String message;
    public Object data;

    public ApiResponse(boolean status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
