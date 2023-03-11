package com.starlight.huggy.exception;

public class HuggyException extends Exception{
    public HuggyException(String message) {
        super(message);
    }

    public HuggyException(BaseExceptionType googleConnectionError) {
        super(googleConnectionError.getMessage());
    }
}
