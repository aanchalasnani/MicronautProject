package com.example.service;

import com.example.entity.Author;
import com.example.entity.Book;
import com.example.exception.BadRequestException;
import com.example.repository.AuthorRepository;
import com.example.repository.BookAuthorRepository;
import com.example.repository.BookRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@MicronautTest
public class ProjectServiceTest {

    @Mock
    AuthorRepository authorRepository;

    @Mock
    BookRepository bookRepository;

    @Mock
    BookAuthorRepository bookAuthorRepository;
    ProjectService service;
    @BeforeEach
    void setup() {
        initMocks(this);
        service = new ProjectService(authorRepository, bookRepository, bookAuthorRepository);
    }


    @Test
    public void testGetAllAuthors() {
        // Arrange
        List<Author> expectedAuthors = new ArrayList<>();
        expectedAuthors.add(new Author(1, "John Doe", "john.doe@example.com"));
        expectedAuthors.add(new Author(2, "Jane Smith", "jane.smith@example.com"));
        when(authorRepository.findAll()).thenReturn(expectedAuthors);

        // Act
        List<Author> actualAuthors = service.getAllAuthors();

        // Assert
        assertEquals(expectedAuthors, actualAuthors);

    }

    @Test
    public void testGetAllBooks() {
        //arrange
        List<Book> expectedBooks = new ArrayList<>() ;
        expectedBooks.add(new Book(1, "Object oriented programming", 380, 178654));
        expectedBooks.add(new Book(2, "Programming in C++", 400, 198654));

        when(bookRepository.findAll()).thenReturn(expectedBooks);

        List<Book> actualResponse = service.getAllBooks();

        assertEquals(expectedBooks, actualResponse);

    }

    @Test
    public void testAddBook() throws BadRequestException {

        //scenario:1 => adding valid book
        Book expectedBook = new Book(1, "Object oriented programming", 850, 189761);
        when(bookRepository.save(expectedBook)).thenReturn(expectedBook);
        Book actualBook = service.addBook(expectedBook);
        assertEquals(expectedBook, actualBook);

        //scenario:2 => book with same name
        Book expedtedBookWithSameName= new Book(2, "Object oriented programming", 760, 174231);
        when(bookRepository.checkDuplicateBookName(expedtedBookWithSameName.getName())).thenReturn(Optional.of(expectedBook));
        Exception badRequestExceptionSameBook = assertThrows(BadRequestException.class, ()->service.addBook(expedtedBookWithSameName));

        //scenario:3 => book with same isbn
        Book expectedBookWithSameIsbn = new Book(3, "Programmin in C++", 390, 189761);
        when(bookRepository.findByIsbn(expectedBookWithSameIsbn.getIsbn())).thenReturn(Optional.of(expectedBook));
        Exception badRequestExceptionSameIsbn = assertThrows(BadRequestException.class, ()->service.addBook(expectedBookWithSameIsbn));
    }

//    @Test
//    public void testAddBookException() throws BadRequestException {
//        Book expectedBook = new Book(1, "Object oriented programming", 850, 189761);
//        when(bookRepository.save(expectedBook)).thenReturn(expectedBook);
//        Book actualBook = service.addBook(expectedBook);
//        assertEquals(expectedBook, actualBook);
//        //scenario:3 => book with same isbn
//        Book expectedBookWithSameIsbn = new Book(3, "Programmin in C++", 390, 189761);
//        when(bookRepository.findByIsbn(expectedBookWithSameIsbn.getIsbn())).thenReturn(Optional.of(expectedBook));
//        Exception badRequestExceptionSameIsbn = assertThrows(BadRequestException.class, ()->service.addBook(expedtedBookWithSameName));
//    }
}