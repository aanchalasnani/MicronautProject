package com.example.service;

import com.example.entity.Author;
import com.example.entity.Book;
import com.example.model.dto.AuthorDTO;
import com.example.model.dto.BookDTO;
import com.example.repository.AuthorRepository;
import com.example.repository.BookAuthorRepository;
import com.example.repository.BookRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Inject
    Service service;

    @BeforeEach
    void setup() {
        initMocks(this);
    }


    @Test
    public void getAllAuthorsTest() {

        // Mock data
        List<Author> authors = new ArrayList<>();
        authors.add(new Author(1, "John Doe", "john@example.com"));
        when(authorRepository.findAll()).thenReturn(authors);

        List<AuthorDTO> result = service.getAllAuthors();

        assertEquals(12, result.size());
//        AuthorDTO authorDTO = result.get(0);
//        assertEquals(1, authorDTO.getId());
//        assertEquals("John Doe", authorDTO.getName());
//        assertEquals("john@example.com", authorDTO.getEmail());


//        verify(authorRepository, times(1)).findAll();

    }

    @Test
    public void getAllBooksTest() {

        // Mock data
        List<Book> books = new ArrayList<>();
        books.add(new Book(1, "John Doe", 12, 123456));
        when(bookRepository.findAll()).thenReturn(books);

        List<BookDTO> result = service.getAllBooks();

        assertEquals(25, result.size());
//        AuthorDTO authorDTO = result.get(0);
//        assertEquals(1, authorDTO.getId());
//        assertEquals("John Doe", authorDTO.getName());
//        assertEquals("john@example.com", authorDTO.getEmail());


//        verify(authorRepository, times(1)).findAll();

    }

}