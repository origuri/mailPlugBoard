package com.example.mailplugboard.model.httpResponse;

import lombok.Data;

@Data
public class HttpResponseDto {

    private int httpStatus;
    private String httpMassage;


    public HttpResponseDto(int httpStatus, String httpMassage) {
        this.httpStatus = httpStatus;
        this.httpMassage = httpMassage;
    }
}
