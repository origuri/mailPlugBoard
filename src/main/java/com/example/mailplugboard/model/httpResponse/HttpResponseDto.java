package com.example.mailplugboard.model.httpResponse;

import lombok.Data;

@Data
public class HttpResponseDto<T> {

    private int httpStatus;
    private String httpMassage;
    private T data;


    public HttpResponseDto(int httpStatus, String httpMassage) {
        this.httpStatus = httpStatus;
        this.httpMassage = httpMassage;
    }

    public HttpResponseDto(int httpStatus, String httpMassage, T data) {
        this.httpStatus = httpStatus;
        this.httpMassage = httpMassage;
        this.data = data;
    }
}
