package com.nequard.common;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(SecurityException.class)
    ResponseEntity<Map<String,String>> security(SecurityException e) {
        String code = e.getMessage() == null ? "ACCESS_DENIED" : e.getMessage();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", code));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<Map<String,String>> illegal(IllegalArgumentException e) {
        String code = e.getMessage() == null ? "INVALID_REQUEST" : e.getMessage();
        return ResponseEntity.badRequest().body(Map.of("error", code));
    }
}
