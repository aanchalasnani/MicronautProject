package com.example.repository;

import com.example.entity.Author;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Integer> {
    public Optional<Author> findByEmailId(String emailId);
}
