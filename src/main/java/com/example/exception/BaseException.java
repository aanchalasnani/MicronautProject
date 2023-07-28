package com.example.exception;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class BaseException extends Exception {

    private final String code;
    private final Set<String> messages = new HashSet<>();
    private final int status;

    public BaseException(String message, Throwable cause, String code, int status) {
        super(message, cause);
        this.code = code;
        this.status = status;
        this.messages.add(message);
    }

    public BaseException(String message, String code, int status) {
        super(message);
        this.code = code;
        this.status = status;
        this.messages.add(message);
    }

    public BaseException(Set<String> messages, String code, int status) {
        super(String.join(" \n ", messages));
        this.code = code;
        this.status = status;
        this.messages.addAll(messages);
    }

    public BaseException(Set<String> messages, String code, int status, Throwable cause) {
        super(String.join(" \n ", messages), cause);
        this.code = code;
        this.status = status;
        this.messages.addAll(messages);
    }
}
