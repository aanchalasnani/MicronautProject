package com.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = false)
@IdClass(EmbeddedBookAuthor.class)
@Table(name = "book_author", indexes = {
        @Index(name = "book_id_index", columnList = "book_id"),
        @Index(name = "author_id_index", columnList = "author_id")
})
public class Book_Author {

    @Id
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Id
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    // other fields and methods

}