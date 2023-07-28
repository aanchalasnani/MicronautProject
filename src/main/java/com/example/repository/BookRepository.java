package com.example.repository;

import com.example.entity.Book;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface BookRepository extends CrudRepository<Book, Integer> {

    @Query(value = "SELECT * FROM book WHERE name = :bookName", nativeQuery = true)
    public Optional<Book> checkDuplicateBookName(String bookName);

    public Optional<Book> findByIsbn(int isbn);



}
