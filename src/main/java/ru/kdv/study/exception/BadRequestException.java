package ru.kdv.study.exception;

public class BadRequestException extends RuntimeException{

    public static BadRequestException create(final String message) {
        return new BadRequestException(message);
    }

    public BadRequestException(String message) {
        super(message);
    }
}
