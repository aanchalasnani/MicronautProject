package com.example.repository;

import com.example.entity.Book_Author;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface BookAuthorRepository extends CrudRepository<Book_Author, Integer> {

    @Query(value = "SELECT book_id FROM book_author WHERE author_id = :authorId", nativeQuery = true)
    List<Integer> findBookAuthorMapping(int authorId);

    @Query(value = "INSERT INTO book_author VALUES (:bookId, :authorId)", nativeQuery = true)
    void addBookAuthor(int bookId, int authorId);
}
