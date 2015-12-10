package com.example.cm.cm_web.exceptions;

public class ImageUploadException extends RuntimeException {
    private String message;
    private String className;

    public ImageUploadException(String message, String className){
        this.message = message;
        this.className = className;
    }

    public String getMessage(){
        return message;
    }

    public String getClassName(){
        return className;
    }
}
