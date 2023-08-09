package com.example.entity;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = false)
@Serdeable
@Table(name = "book_new", indexes = {
        @Index(name = "book_id_index", columnList = "id")
}, uniqueConstraints = @UniqueConstraint(columnNames = {"isbn", "name"}))
public class Book {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true, name = "name")
    private String name;

    private Integer price;

    @Column(unique = true, name = "isbn")
    private Integer isbn;

}
