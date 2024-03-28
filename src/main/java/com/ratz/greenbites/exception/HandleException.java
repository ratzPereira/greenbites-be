package com.ratz.greenbites.exception;

import com.ratz.greenbites.response.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.webjars.NotFoundException;

import java.nio.file.AccessDeniedException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

@RestControllerAdvice
@Slf4j
public class HandleException extends ResponseEntityExceptionHandler implements ErrorController {


    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatusCode statusCode, WebRequest request) {
        log.error("Error message: {}", ex.getMessage());
        return new ResponseEntity<>(HttpResponse.builder()
                .timeStamp(now().toString())
                .reason(ex.getMessage())
                .developerMessage(ex.getMessage())
                .statusCode(statusCode.value())
                .httpStatus(HttpStatus.resolve(statusCode.value()))
                .build(), statusCode);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatusCode statusCode, WebRequest request) {
        log.error("Error message: {}", ex.getMessage());
        List<FieldError> fieldErrorList = ex.getBindingResult().getFieldErrors();

        String fieldMessage = fieldErrorList.stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return new ResponseEntity<>(HttpResponse.builder()
                .timeStamp(now().toString())
                .reason(fieldMessage)
                .developerMessage(ex.getMessage())
                .statusCode(statusCode.value())
                .httpStatus(HttpStatus.resolve(statusCode.value()))
                .build(), statusCode);
    }

    //custom
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<HttpResponse> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex) {
        log.error("Error message: {}", ex.getMessage());
        return new ResponseEntity<>(HttpResponse.builder()
                .timeStamp(now().toString())
                .reason(ex.getMessage())
                .developerMessage(ex.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpResponse> handleBadCredentialsException(BadCredentialsException ex) {
        log.error("Error message: {}", ex.getMessage());
        return new ResponseEntity<>(HttpResponse.builder()
                .timeStamp(now().toString())
                .reason(ex.getMessage() + ", Invalid username or password")
                .developerMessage(ex.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<HttpResponse> handleApiException(ApiException ex) {
        log.error("Error message: {}", ex.getMessage());
        return new ResponseEntity<>(HttpResponse.builder()
                .timeStamp(now().toString())
                .reason(ex.getMessage())
                .developerMessage(ex.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpResponse> handleAccessDeniedException(AccessDeniedException ex) {
        log.error("Error message: {}", ex.getMessage());
        return new ResponseEntity<>(HttpResponse.builder()
                .timeStamp(now().toString())
                .reason("Access denied")
                .developerMessage(ex.getMessage())
                .statusCode(HttpStatus.FORBIDDEN.value())
                .httpStatus(HttpStatus.FORBIDDEN)
                .build(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse> handleException(Exception ex) {
        log.error("Error message: {}", ex.getMessage());
        return new ResponseEntity<>(HttpResponse.builder()
                .timeStamp(now().toString())
                .reason("Internal server error")
                .developerMessage(ex.getMessage())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<HttpResponse> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex) {
        log.error("Error message: {}", ex.getMessage());
        return new ResponseEntity<>(HttpResponse.builder()
                .timeStamp(now().toString())
                .reason(ex.getMessage().contains("expected 1 actual 0") ? "Record not found" : ex.getMessage())
                .developerMessage(ex.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<HttpResponse> handleDisabledException(DisabledException ex) {
        log.error("Error message: {}", ex.getMessage());
        return new ResponseEntity<>(HttpResponse.builder()
                .timeStamp(now().toString())
                .reason(ex.getMessage())
                .developerMessage(ex.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<HttpResponse> handleLockedException(LockedException ex) {
        log.error("Error message: {}", ex.getMessage());
        return new ResponseEntity<>(HttpResponse.builder()
                .timeStamp(now().toString())
                .reason(ex.getMessage())
                .developerMessage(ex.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<HttpResponse> handleNotFoundException(NotFoundException ex) {
        log.error("Error message: {}", ex.getMessage());
        return new ResponseEntity<>(HttpResponse.builder()
                .timeStamp(now().toString())
                .reason(ex.getMessage())
                .developerMessage(ex.getMessage())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .httpStatus(HttpStatus.NOT_FOUND)
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<HttpResponse> handleForbiddenException(ForbiddenException ex) {
        log.error("Error message: {}", ex.getMessage());
        return new ResponseEntity<>(HttpResponse.builder()
                .timeStamp(now().toString())
                .reason(ex.getMessage())
                .developerMessage(ex.getMessage())
                .statusCode(HttpStatus.FORBIDDEN.value())
                .httpStatus(HttpStatus.FORBIDDEN)
                .build(), HttpStatus.FORBIDDEN);
    }
}
