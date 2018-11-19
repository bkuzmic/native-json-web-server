package com.github.bkuzmic.web.db.exception;

public class DbRuntimeException extends RuntimeException {

    public DbRuntimeException(String message) {
        super(message);
    }

    public DbRuntimeException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public DbRuntimeException(Throwable throwable) {
        super(throwable);
    }

}
