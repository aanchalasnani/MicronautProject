package com.example.model.dto;

import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Serdeable
public class BookDTO implements Serializable {
    int id;
    String name;
    int isbn;
    int price;


    public BookDTO(String name, int isbn, int price) {
        this.isbn = isbn;
        this.name = name;
        this.price = price;
    }
}
