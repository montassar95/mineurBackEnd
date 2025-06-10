//package com.cgpr.mineur.handel;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import com.cgpr.mineur.exception.InvalidPasswordException;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(InvalidPasswordException.class)
//    public ResponseEntity<Map<String, String>> handleInvalidPasswordException(InvalidPasswordException ex) {
//        Map<String, String> error = new HashMap<>();
//        error.put("oldPassword", ex.getMessage());
//        return ResponseEntity.badRequest().body(error);
//    }
//
//    // autres handlers...
//}
