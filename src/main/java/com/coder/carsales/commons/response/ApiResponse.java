package com.coder.carsales.commons.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApiResponse<T> {

    private boolean success ;
    private String message;
    private T data ;
    private int statusCode ;
    private LocalDateTime timestamp ;

    public ApiResponse(boolean success , String message , T data , int statusCode){
        this.success = success ;
        this.statusCode = statusCode;
        this.message = message ;
        this.data = data ;
        this.timestamp = LocalDateTime.now() ;

    }
}
