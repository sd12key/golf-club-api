package org.alvio.golfnode.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ValidationErrorHandler {

    // Handles @RequestBody validation errors (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException ex) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // Handles @RequestBody validation errors (@Valid) in bulk payload
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<Map<String, String>> handleHandlerMethodValidation(HandlerMethodValidationException ex) {
        MessageSourceResolvable firstError = ex.getAllErrors().get(0);
        String fieldName = "error";
        String errorMessage = firstError.getDefaultMessage();
        if (firstError instanceof FieldError fieldError) {
            fieldName = fieldError.getField();
        }
        else if (firstError instanceof ObjectError objectError) {
            if (!objectError.getCodes()[0].contains(".")) {
                fieldName = objectError.getCodes()[0];
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(fieldName, errorMessage));
    }

    // Handles @RequestParam validation errors (@Validated)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleParamValidationErrors(
            ConstraintViolationException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String path = violation.getPropertyPath().toString();
            String paramName = path.substring(path.lastIndexOf('.') + 1);
            errors.put(paramName, violation.getMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, String>> handleMissingParam(MissingServletRequestParameterException ex) {
        Map<String, String> error = new HashMap<>();
        error.put(ex.getParameterName(), "Required parameter '" + ex.getParameterName() + "' is missing.");
        return ResponseEntity.badRequest().body(error);
    }

    // handles errors occurring with invalid JSON or date formats
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        if (ex.getCause() != null && ex.getCause().getCause() instanceof DateTimeParseException) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Invalid date format (use yyyy-mm-dd).");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // default handling for other HttpMessageNotReadable cases
        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid request format");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // handles type mismatch errors for @RequestParam
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, String> error = new HashMap<>();

        if (ex.getRequiredType() != null && ex.getRequiredType().getSimpleName().equals("LocalDate")) {
            error.put(ex.getName(), "Invalid date format. Expected: yyyy-mm-dd.");
        } else {
            error.put(ex.getName(), "Invalid value for parameter.");
        }

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(NoSuchElementException ex) {
//        System.out.println("Handled by custom ValidationErrorHandler: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String, String>> handleConflict(ConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", ex.getClass().getSimpleName() + ": " + ex.getMessage()));
    }

}