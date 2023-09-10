package com.example.service;

import com.example.entity.Author;
import com.example.entity.Book;
import com.example.entity.Book_Author;
import com.example.exception.NotFoundException;
import com.example.model.dto.AuthorDTO;
import com.example.model.dto.BookAuthorDTO;
import com.example.model.dto.BookDTO;
import com.example.model.request.AddBookAuthorRequest;
import com.example.model.response.AddBookResponse;
import com.example.repository.AuthorRepository;
import com.example.repository.BookAuthorRepository;
import com.example.repository.BookRepository;
import com.example.service.clients.RedisCacheClient;
import com.example.service.impl.ServiceImpl;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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

    @Mock
    RedisCacheClient redisCacheClient;

    ServiceImpl service;

    @BeforeEach
    void setup() {
        initMocks(this);
        service = new ServiceImpl(authorRepository, bookRepository, bookAuthorRepository, redisCacheClient);
    }

    @Test
    public void testGetAllBooksWithCachedData() {
        when(redisCacheClient.exists("allBooks")).thenReturn(true);
        when(redisCacheClient.get("allBooks")).thenReturn("cachedJsonData");

        List<BookDTO> result = service.getAllBooks();

        assertNotNull(result);
    }

    @Test
    public void testGetAllBooksWithoutCachedData() {
        when(redisCacheClient.exists("allBooks")).thenReturn(false);

        List<Book> mockBooks = new ArrayList<>();
        Book book = new Book();
        book.setId(1);
        book.setName("Sample Book");
        book.setPrice(20);
        book.setIsbn(123456);
        mockBooks.add(book);
        when(bookRepository.findAll()).thenReturn(mockBooks);

        List<BookDTO> result = service.getAllBooks();
        assertNotNull(result);
    }

    @Test
    public void testGetAllAuthorsWithCachedData() {
        when(redisCacheClient.exists("allAuthors")).thenReturn(true);
        when(redisCacheClient.get("allAuthors")).thenReturn("cachedJsonData");

        List<AuthorDTO> result = service.getAllAuthors();

        assertNotNull(result);
    }

    @Test
    public void testGetAllAuthorsWithoutCachedData() {
        when(redisCacheClient.exists("allAuthors")).thenReturn(false);

        List<Author> mockAuthors = new ArrayList<>();
        Author author = new Author();
        author.setId(1);
        author.setEmailId("example@gmail.com");
        author.setName("dummy");
        mockAuthors.add(author);
        when(authorRepository.findAll()).thenReturn(mockAuthors);

        List<AuthorDTO> result = service.getAllAuthors();

        assertNotNull(result);
    }

    @Test
    public void testAddBookByAuthorIds() throws NotFoundException {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(1);
        bookDTO.setName("Sample Book");
        bookDTO.setIsbn(123456);
        bookDTO.setPrice(20);
        Book savedBook = new Book();
        savedBook.setId(1);
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        Author existingAuthor = new Author();
        existingAuthor.setId(101);
        when(authorRepository.findById(101)).thenReturn(Optional.of(existingAuthor));

        List<Integer> authorIds = List.of(101); // Existing author's ID

        AddBookResponse result = service.addBookByAuthorIds(bookDTO, authorIds);

        assertEquals("Success", result.getStatus());
        assertEquals(1, result.getBookId());

        verify(bookRepository).save(any(Book.class));
        verify(authorRepository).findById(101);
        verify(bookAuthorRepository).save(any(Book_Author.class));
    }

    @Test
    public void testAddBookAuthor() throws NotFoundException {
        AddBookAuthorRequest addBookAuthorRequest = new AddBookAuthorRequest(1,2);

        Book book = new Book();
        Author author = new Author();
        when(bookRepository.findById(2)).thenReturn(Optional.of(book));
        when(authorRepository.findById(1)).thenReturn(Optional.of(author));

        Book_Author savedBookAuthor = new Book_Author(book, author);
        when(bookAuthorRepository.save(any(Book_Author.class))).thenReturn(savedBookAuthor);

        BookAuthorDTO result = service.addBookAuthor(addBookAuthorRequest);

        assertNotNull(result);

        verify(bookRepository).findById(2);
        verify(authorRepository).findById(1);
        verify(bookAuthorRepository).save(any(Book_Author.class));
    }

//    @Test
//    public void testAddAuthor() {
//        AddAuthorRequest addAuthorRequest = new AddAuthorRequest("dummy", "dummy@gmail.com");
//
//        AuthorDTO authorDTO = new AuthorDTO();
//        authorDTO.setName(addAuthorRequest.getName());
//        authorDTO.setEmail(addAuthorRequest.getEmailId());
//        when(service.addAnAuthor(authorDTO)).thenReturn(new AddAuthorResponse());
//
//        AddAuthorResponse result = service.addAuthor(addAuthorRequest);
//
//        assertNotNull(result);
//        assertEquals("Success", result.getStatus());
//
//        verify(service).addAnAuthor(authorDTO);
//    }

}