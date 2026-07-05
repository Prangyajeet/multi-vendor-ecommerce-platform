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
import com.prangyajeet.mvep.exception.CartAlreadyExistsException;
import com.prangyajeet.mvep.exception.CartNotFoundException;
import com.prangyajeet.mvep.exception.InsufficientStockException;
import com.prangyajeet.mvep.exception.WishlistNotFoundException;
import com.prangyajeet.mvep.exception.ReviewAlreadyExistsException;
import com.prangyajeet.mvep.exception.ReviewNotFoundException;
import com.prangyajeet.mvep.exception.ProductNotPurchasedException;
import com.prangyajeet.mvep.exception.UnauthorizedReviewException;
import com.prangyajeet.mvep.exception.UserNotFoundException;
import com.prangyajeet.mvep.exception.CouponAlreadyExistsException;
import com.prangyajeet.mvep.exception.CouponExpiredException;
import com.prangyajeet.mvep.exception.CouponInactiveException;
import com.prangyajeet.mvep.exception.CouponNotFoundException;
import com.prangyajeet.mvep.exception.InvalidCouponException;
import com.prangyajeet.mvep.response.ApiResponse;
import com.prangyajeet.mvep.exception.PaymentNotFoundException;
import com.prangyajeet.mvep.exception.PaymentAlreadyExistsException;
import com.prangyajeet.mvep.exception.InvalidPaymentAmountException;
import com.prangyajeet.mvep.exception.CashfreePaymentException;

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
 
//===========================================
//CART NOT FOUND
//===========================================

@ExceptionHandler(CartNotFoundException.class)
public ResponseEntity<Map<String, Object>> handleCartNotFoundException(
      CartNotFoundException ex) {

  Map<String, Object> response = new LinkedHashMap<>();

  response.put("timestamp", LocalDateTime.now());
  response.put("status", HttpStatus.NOT_FOUND.value());
  response.put("message", ex.getMessage());

  return new ResponseEntity<>(
          response,
          HttpStatus.NOT_FOUND
  );
}

//===========================================
//CART ALREADY EXISTS
//===========================================

@ExceptionHandler(CartAlreadyExistsException.class)
public ResponseEntity<Map<String, Object>> handleCartAlreadyExistsException(
     CartAlreadyExistsException ex) {

 Map<String, Object> response = new LinkedHashMap<>();

 response.put("timestamp", LocalDateTime.now());
 response.put("status", HttpStatus.CONFLICT.value());
 response.put("message", ex.getMessage());

 return new ResponseEntity<>(
         response,
         HttpStatus.CONFLICT
 );
}

//===========================================
//INSUFFICIENT STOCK
//===========================================

@ExceptionHandler(InsufficientStockException.class)
public ResponseEntity<Map<String, Object>> handleInsufficientStockException(
     InsufficientStockException ex) {

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
    
    @ExceptionHandler(WishlistNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleWishlistNotFoundException(
            WishlistNotFoundException ex) {

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("message", ex.getMessage());

        return new ResponseEntity<>(
                response,
                HttpStatus.NOT_FOUND
        );
    }
    
    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleReviewNotFoundException(
            ReviewNotFoundException ex) {

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("message", ex.getMessage());

        return new ResponseEntity<>(
                response,
                HttpStatus.NOT_FOUND
        );
    } 
    
    @ExceptionHandler(ReviewAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleReviewAlreadyExistsException(
            ReviewAlreadyExistsException ex) {

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", ex.getMessage());

        return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST
        );
    }
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundException(
            UserNotFoundException ex) {

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("message", ex.getMessage());

        return new ResponseEntity<>(
                response,
                HttpStatus.NOT_FOUND
        );
    }
    
    @ExceptionHandler(UnauthorizedReviewException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedReviewException(
            UnauthorizedReviewException ex) {

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.FORBIDDEN.value());
        response.put("message", ex.getMessage());

        return new ResponseEntity<>(
                response,
                HttpStatus.FORBIDDEN
        );
    }
    
    @ExceptionHandler(ProductNotPurchasedException.class)
    public ResponseEntity<Map<String, Object>> handleProductNotPurchasedException(
            ProductNotPurchasedException ex) {

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", ex.getMessage());

        return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST
        );
    }
    
    @ExceptionHandler(CouponAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleCouponAlreadyExistsException(
            CouponAlreadyExistsException ex) {

        ApiResponse<Void> response =
                new ApiResponse<>(
                        false,
                        ex.getMessage(),
                        null
                );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }
    
    @ExceptionHandler(CouponNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleCouponNotFoundException(
            CouponNotFoundException ex) {

        ApiResponse<Void> response =
                new ApiResponse<>(
                        false,
                        ex.getMessage(),
                        null
                );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }
    
    @ExceptionHandler(CouponExpiredException.class)
    public ResponseEntity<ApiResponse<Void>> handleCouponExpiredException(
            CouponExpiredException ex) {

        ApiResponse<Void> response =
                new ApiResponse<>(
                        false,
                        ex.getMessage(),
                        null
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
    
    @ExceptionHandler(CouponInactiveException.class)
    public ResponseEntity<ApiResponse<Void>> handleCouponInactiveException(
            CouponInactiveException ex) {

        ApiResponse<Void> response =
                new ApiResponse<>(
                        false,
                        ex.getMessage(),
                        null
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
    
    @ExceptionHandler(InvalidCouponException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidCouponException(
            InvalidCouponException ex) {

        ApiResponse<Void> response =
                new ApiResponse<>(
                        false,
                        ex.getMessage(),
                        null
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
    
    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handlePaymentNotFoundException(
            PaymentNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.failure(ex.getMessage()));
    }
    
    @ExceptionHandler(PaymentAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<String>> handlePaymentAlreadyExistsException(
            PaymentAlreadyExistsException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.failure(ex.getMessage()));
    }
    
    @ExceptionHandler(InvalidPaymentAmountException.class)
    public ResponseEntity<ApiResponse<String>> handleInvalidPaymentAmountException(
            InvalidPaymentAmountException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure(ex.getMessage()));
    }
    
    @ExceptionHandler(CashfreePaymentException.class)
    public ResponseEntity<ApiResponse<String>> handleCashfreePaymentException(
            CashfreePaymentException ex) {

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(ApiResponse.failure(ex.getMessage()));
    }
    
    
}

