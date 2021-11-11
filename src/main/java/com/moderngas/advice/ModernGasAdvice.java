package com.moderngas.advice;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ModernGasAdvice {

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ResponseStatus> handleCustomExceptionForInvalidInput(BadRequestException badRequestException) {
        ResponseStatus status = new ResponseStatus();
        status.setStatus(badRequestException.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(status);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<ResponseStatus> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        ResponseStatus status = new ResponseStatus();

        status.setStatus(String.format("Required parameter %s is not present", exception.getParameterName()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(status);
    }

    @ExceptionHandler({MissingServletRequestPartException.class})
    public ResponseEntity<ResponseStatus> handleMissingServletRequestPartException(MissingServletRequestPartException exception) {
        ResponseStatus status = new ResponseStatus();
        status.setStatus(String.format("Required request part %s is not present", exception.getRequestPartName()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(status);
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    public ResponseEntity<ResponseStatus> handleNoHandlerFoundException(NoHandlerFoundException exception) {
        ResponseStatus status = new ResponseStatus();
        status.setStatus(String.format("No handler found for %s %s", exception.getHttpMethod(), exception.getRequestURL()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(status);
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ResponseStatus> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        ResponseStatus status = new ResponseStatus();
        status.setStatus(String.format("Request method %s not supported", exception.getMethod()));
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(status);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ResponseStatus> handleAccessDeniedException(AccessDeniedException accessDeniedException) {
        ResponseStatus status = new ResponseStatus();
        status.setStatus("Access Restricted");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(status);
    }

    @ExceptionHandler({TokenExpiredException.class})
    public ResponseEntity<ResponseStatus> handleTokenExpiredException(TokenExpiredException tokenExpiredException) {
        ResponseStatus status = new ResponseStatus();
        status.setStatus("Request Token Has Expired");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(status);
    }

}
