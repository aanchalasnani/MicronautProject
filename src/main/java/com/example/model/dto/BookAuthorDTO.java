package com.example.model.dto;

import com.example.entity.Author;
import com.example.entity.Book;
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
public class BookAuthorDTO {
    private int id;
    private Book book;
    private Author author;
}
