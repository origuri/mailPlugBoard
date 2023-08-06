package com.example.mailplugboard.controller.error;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class WebErrorController implements ErrorController {
    @GetMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        log.info("status -> {} ",status);

        if(status != null){
            // 문자를 int로 바꿈
            int statusCode = Integer.valueOf(status.toString());

            // statusCode, HttpStatus.NOT_FOUND.value() 둘다 int type
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error/404";
            } else if(statusCode == HttpStatus.METHOD_NOT_ALLOWED.value()) {
                log.info("HttpStatus.METHOD_NOT_ALLOWED.value() -> {} ",HttpStatus.METHOD_NOT_ALLOWED.value());
                return "error/405";
            }
        }

        return "error/404";
    }


}
