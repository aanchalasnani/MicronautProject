package com.example.entity;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Serdeable
@Table(name = "book_author", indexes = {
        @Index(name = "book_id_index", columnList = "book_id"),
        @Index(name = "author_id_index", columnList = "author_id")
}, uniqueConstraints = {
        @UniqueConstraint(name = "book_author_uniq", columnNames = {"book_id", "author_id"})})
public class Book_Author {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    public Book_Author(Book book, Author author) {
        this.book = book;
        this.author = author;
    }

}