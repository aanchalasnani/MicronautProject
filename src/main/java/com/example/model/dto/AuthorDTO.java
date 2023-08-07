package com.example.model.dto;

import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Serdeable
public class AuthorDTO {
    private int id;
    private String name;
    private String email;

    public AuthorDTO(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
