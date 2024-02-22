package com.example.commentservice.exception.handler;

import com.example.commentservice.exception.CommentServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = CommentServiceException.class)
    public ResponseEntity handleInvalidDataException(CommentServiceException ex) {
        return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity handleInvalidArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
