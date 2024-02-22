package com.example.commentservice.exception;

public class CommentServiceException extends Exception {
    public CommentServiceException(String msg) {
        super(msg);
    }

    public CommentServiceException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
