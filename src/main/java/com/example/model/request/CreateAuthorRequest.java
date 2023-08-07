package com.example.model.request;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Introspected
@Serdeable
public class CreateAuthorRequest {
    private final String name;
    private final String emailId;
    private final List<Integer> bookIds;
}
