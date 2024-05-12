package org.galaxy.tasktrackerapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.galaxy.tasktrackerapi.exception.TaskNotFoundException;
import org.galaxy.tasktrackerapi.exception.UserAlreadyExistException;
import org.galaxy.tasktrackerapi.exception.UserNotFoundException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.Locale;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ControllerExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ProblemDetail> handleBindException(org.springframework.validation.BindException bindException, Locale locale) {
        var problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, messageSource.getMessage("errors.400.title", new Object[0],
                        "errors.400.title", locale));
        problemDetail.setProperty("errors", bindException.getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .toList());
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ProblemDetail> handleUserAlreadyExistException(UserAlreadyExistException exception, Locale locale) {
        var problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, messageSource.getMessage("errors.409.title", new Object[0],
                        "errors.409.title", locale));
        problemDetail.setProperty("errors", exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }

    @ExceptionHandler({UserNotFoundException.class, TaskNotFoundException.class})
    public ResponseEntity<ProblemDetail> handleUserNotFoundException(Exception exception, Locale locale) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, messageSource.getMessage("errors.404.title", new Object[0],
                        "errors.404.title", locale));
        problemDetail.setProperty("errors", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleException(Exception exception, Locale locale) {
        log.error(exception.getMessage(), exception);
        var problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.INTERNAL_SERVER_ERROR, messageSource.getMessage("errors.500.title", new Object[0],
                "errors.500.title", locale));
        problemDetail.setProperty("errors", messageSource.getMessage("errors.500.description", new Object[0],
                "errors.500.description", locale));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }

}