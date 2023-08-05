package com.example.mailplugboard.model.httpResponse;

import lombok.Getter;

@Getter
public enum HttpResponseInfo {
    NOT_FOUND(404, "찾을 수 없음"),
    BAD_REQUEST(400, "잘못된 요청"),
    INTERNAL_SERVER_ERROR(500, "내부 서버 오류"),
    OK(200,"성공");

    private final int statusCode;
    private final String message;

    HttpResponseInfo(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }


}
