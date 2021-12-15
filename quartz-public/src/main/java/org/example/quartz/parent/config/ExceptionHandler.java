package org.example.quartz.parent.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e, HttpServletRequest request) {
        //All exception logs here
        log.error(e.getMessage(), e);

        Map<String, Object> data = toInternalExceptionMessage(e, request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(data);
    }

    public static Map<String, Object> toInternalExceptionMessage(Exception exception, HttpServletRequest request) {
        Map<String, Object> data = new HashMap<>();
        data.put("timestamp", new Date());
        data.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        data.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase() + ", " + exception.getMessage());
        data.put("path", request.getRequestURI());
        return data;
    }
}
