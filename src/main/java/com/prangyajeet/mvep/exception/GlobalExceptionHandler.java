package com.prangyajeet.mvep.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.prangyajeet.mvep.exception.CategoryNotFoundException;
import com.prangyajeet.mvep.exception.ProductNotFoundException;
import com.prangyajeet.mvep.exception.VendorNotFoundException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ===========================================
    // VALIDATION EXCEPTION
    // ===========================================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());

        Map<String, String> errors = new LinkedHashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        response.put("errors", errors);

        return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST
        );
    }
    
 // ===========================================
 // PRODUCT NOT FOUND
 // ===========================================

 @ExceptionHandler(ProductNotFoundException.class)
 public ResponseEntity<Map<String, Object>> handleProductNotFoundException(
         ProductNotFoundException ex) {

     Map<String, Object> response = new LinkedHashMap<>();

     response.put("timestamp", LocalDateTime.now());
     response.put("status", HttpStatus.NOT_FOUND.value());
     response.put("message", ex.getMessage());

     return new ResponseEntity<>(
             response,
             HttpStatus.NOT_FOUND
     );
 }

 // ===========================================
 // CATEGORY NOT FOUND
 // ===========================================

 @ExceptionHandler(CategoryNotFoundException.class)
 public ResponseEntity<Map<String, Object>> handleCategoryNotFoundException(
         CategoryNotFoundException ex) {

     Map<String, Object> response = new LinkedHashMap<>();

     response.put("timestamp", LocalDateTime.now());
     response.put("status", HttpStatus.NOT_FOUND.value());
     response.put("message", ex.getMessage());

     return new ResponseEntity<>(
             response,
             HttpStatus.NOT_FOUND
     );
 }

 // ===========================================
 // VENDOR NOT FOUND
 // ===========================================

 @ExceptionHandler(VendorNotFoundException.class)
 public ResponseEntity<Map<String, Object>> handleVendorNotFoundException(
         VendorNotFoundException ex) {

     Map<String, Object> response = new LinkedHashMap<>();

     response.put("timestamp", LocalDateTime.now());
     response.put("status", HttpStatus.NOT_FOUND.value());
     response.put("message", ex.getMessage());

     return new ResponseEntity<>(
             response,
             HttpStatus.NOT_FOUND
     );
 }

    // ===========================================
    // RUNTIME EXCEPTION
    // ===========================================

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(
            RuntimeException ex) {

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", ex.getMessage());

        return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST
        );
    }

    // ===========================================
    // GENERAL EXCEPTION
    // ===========================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(
            Exception ex) {

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("message", ex.getMessage());

        return new ResponseEntity<>(
                response,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}