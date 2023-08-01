package com.example.entity;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Serdeable
@EqualsAndHashCode(callSuper = false)
@Table(name = "book", indexes = {
        @Index(name = "book_id_index", columnList = "id")
}, uniqueConstraints = @UniqueConstraint(columnNames = {"isbn"}))
public class Book {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private Integer price;

    @Column(unique = true, name = "isbn")
    private Integer isbn;

}
