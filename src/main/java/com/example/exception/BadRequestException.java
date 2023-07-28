package com.example.exception;

import java.util.Set;

public class BadRequestException extends BaseException  {
    private static final int HTTP_CODE = 400;

    public BadRequestException(String code, String messages, Throwable cause) { super(messages,cause, code, HTTP_CODE);}
    public BadRequestException(String code, String messages) {
        super(messages, code, HTTP_CODE);
    }

    public  BadRequestException(Set<String> messages, String code, Throwable cause) {
        super(messages, code, HTTP_CODE, cause);
    }

    public BadRequestException(String code, Set<String> messages) {super(messages, code, HTTP_CODE);}


}
