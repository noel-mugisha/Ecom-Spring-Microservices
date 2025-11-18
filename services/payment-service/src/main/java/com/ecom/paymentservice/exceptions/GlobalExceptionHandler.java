package com.ecom.paymentservice.exceptions;

import com.ecom.paymentservice.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private ResponseEntity<ApiErrorResponse> getErrorResponse(
            HttpStatus status, RuntimeException exception
    ) {
        var errorResponse = ApiErrorResponse.builder()
                .statusCode(status.value())
                .errorReason(status.getReasonPhrase())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> validationErrors(
            MethodArgumentNotValidException exception
    ) {
        var status = HttpStatus.BAD_REQUEST;
        Map<String, String> errorMessages = new HashMap<>();

        for (FieldError fieldError : exception.getBindingResult().getFieldErrors())
            errorMessages.put(fieldError.getField(), fieldError.getDefaultMessage());
        var errorResponse = ApiErrorResponse.builder()
                .statusCode(status.value())
                .errorReason(status.getReasonPhrase())
                .message(errorMessages)
                .build();
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> resourceNotFound(
            ResourceNotFoundException exception
    ) {
        return getErrorResponse(HttpStatus.NOT_FOUND, exception);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> resourceAlreadyExists(
            ResourceAlreadyExistsException exception
    ) {
        return getErrorResponse(HttpStatus.CONFLICT, exception);
    }

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ApiErrorResponse> businessException(
            BusinessLogicException exception
    ) {
        return getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception);
    }
}
