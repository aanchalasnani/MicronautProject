package com.example.model.response;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Serdeable
public class AddBookResponse {
    private int  bookId;
    private String status;
    private String errorMessage;

}
