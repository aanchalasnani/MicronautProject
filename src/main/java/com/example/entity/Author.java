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
@Table(name = "author",indexes = {
        @Index(name = "author_id_index", columnList = "id")
}, uniqueConstraints = @UniqueConstraint(columnNames = {"email_id"}))
public class Author {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    @Column(unique = true, name = "email_id")
    private String emailId;

}
