package com.example.warehousemanagement.exception;

import com.example.warehousemanagement.dto.ServiceResponse;
import com.example.warehousemanagement.util.ResponseHandler;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(WarehouseException.class)
    public ResponseEntity<ServiceResponse> handleException(WarehouseException e) {
        if(e.getMessages() == null) {
            logger.error(e.getMessage());
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } else {
            logger.error(e.getMessage());
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, e.getMessages());
        }
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ServiceResponse> handleException(Exception ex) {
        logger.error(ex.getMessage(), ex);
        return ResponseHandler.generateResponse("Unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ServiceResponse> handleAccessDeniedException(AccessDeniedException ex) {
        logger.error(ex.getMessage(), ex);
        return ResponseHandler.generateResponse("Unauthorized", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = { ConstraintViolationException.class })
    public ResponseEntity<Object> violationException(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        logger.error(ex.getMessage(), ex);
        return ResponseHandler.generateObjectResponse("Errors in the request", HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(value = { ValidationException.class })
    public ResponseEntity<ServiceResponse> handleValidationException (ValidationException ex) {
        logger.error(ex.getMessage(), ex);
        return ResponseHandler.generateResponse("Errors in the request", HttpStatus.BAD_REQUEST);
    }
}
