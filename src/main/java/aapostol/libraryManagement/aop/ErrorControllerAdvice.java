package aapostol.libraryManagement.aop;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import aapostol.libraryManagement.exception.BusinessRuleException;
import aapostol.libraryManagement.exception.DuplicateResourceException;

@ControllerAdvice
public class ErrorControllerAdvice {

    // 400 Bad Request - Business rule violations
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<String> handle(BusinessRuleException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage() + " at " + LocalDateTime.now());
    }

    // 409 Conflict - Duplicate resource creation attempts
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<String> handle(DuplicateResourceException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(e.getMessage() + " at " + LocalDateTime.now());
    }

    // 404 Not Found - Resource doesnt exist
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handle(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(e.getMessage() + " at " + LocalDateTime.now());
    }

    // 409 Conflict - Duplicate resource or constraint violation
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handle(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(e.getMessage() + " at " + LocalDateTime.now());
    }

    // 409 Conflict - Database constraint violations (unique, foreign key, etc.)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handle(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body("Data integrity violation: " + e.getMostSpecificCause().getMessage() + " at " + LocalDateTime.now());
    }

    // 400 Bad Request - Validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handle(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(e.getBindingResult().getAllErrors().stream()
                .map(err -> err.getDefaultMessage()).collect(Collectors.joining(", ")) 
                + " at " + LocalDateTime.now());
    }

    // 500 Internal Server Error - Unexpected errors
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handle(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(e.getMessage() + " at " + LocalDateTime.now());
    }

    // 500 Internal Server Error - Catch-all for unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handle(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("An unexpected error occurred: " + e.getMessage() + " at " + LocalDateTime.now());
    }
}
