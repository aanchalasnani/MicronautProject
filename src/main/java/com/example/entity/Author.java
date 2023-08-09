package com.example.entity;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = false)
@Serdeable
@Table(name = "author_new",indexes = {
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
