package com.example.repository;

import com.example.entity.Book_Author;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookAuthorRepository extends CrudRepository<Book_Author, Integer> {

    @Query(value = "SELECT book_id FROM book_author WHERE author_id = :authorId", nativeQuery = true)
    Optional<List<Integer>> findBookAuthorMapping(int authorId);

    @Query(value = "INSERT INTO Book_Author VALUES (:bookId, :authorId)", nativeQuery = true)
    void addBookAuthor(int bookId, int authorId);
}
