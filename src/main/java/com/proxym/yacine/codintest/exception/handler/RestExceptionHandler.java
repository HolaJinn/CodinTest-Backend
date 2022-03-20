package com.proxym.yacine.codintest.exception.handler;

import com.proxym.yacine.codintest.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Error> handleException(CustomException exception) {
        final HttpStatus httpStatus = HttpStatus.valueOf(exception.getErrorCode());
        final Error error = Error.builder()
                .errorCode(exception.getError())
                .errors(exception.getErrors())
                .httpResponse(httpStatus.name())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(error, httpStatus);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Error> handleException(AccessDeniedException exception) {
        final HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        final Error error = Error.builder()
                .errorCode("ACCESS DENIED")
                .httpResponse(httpStatus.name())
                .message("Access is denied")
                .build();
        return new ResponseEntity<>(error, httpStatus);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Error> handleException(RuntimeException exception) {
        final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        final Error error = Error.builder()
                .errorCode("SOMETHING WENT WRONG")
                .httpResponse(httpStatus.name())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(error, httpStatus);
    }
}
