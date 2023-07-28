package com.example.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Embeddable
public class EmbeddedBookAuthor implements Serializable {

    private Integer book;

    private Integer author;

}