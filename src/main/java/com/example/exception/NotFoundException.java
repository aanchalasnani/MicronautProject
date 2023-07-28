package com.example.exception;

import java.util.Set;

public class NotFoundException extends BaseException  {
    private static final int HTTP_CODE = 404;

    public NotFoundException(String code, String messages, Throwable cause) { super(messages,cause, code, HTTP_CODE);}
    public NotFoundException(String code, String messages) {
        super(messages, code, HTTP_CODE);
    }

    public  NotFoundException(Set<String> messages, String code, Throwable cause) {
        super(messages, code, HTTP_CODE, cause);
    }

    public NotFoundException(String code, Set<String> messages) {super(messages, code, HTTP_CODE);}


}
