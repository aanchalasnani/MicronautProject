package com.example.service;

import com.example.entity.Author;
import com.example.model.dto.AuthorDTO;
import com.example.repository.AuthorRepository;
import com.example.repository.BookAuthorRepository;
import com.example.repository.BookRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
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

    @InjectMocks
    Service service;

    @BeforeEach
    void setup() {
        initMocks(this);
    }


    @Test
    public void getAllAuthorsTest() {
        List<Author> authors = new ArrayList<>();
        authors.add(new Author(1, "John Doe", "john@example.com"));
        when(authorRepository.findAll()).thenReturn(authors);

        List<AuthorDTO> result = service.getAllAuthors();

        assertEquals(1, result.size());
        AuthorDTO authorDTO = result.get(0);
        assertEquals(1, authorDTO.getId());
        assertEquals("John Doe", authorDTO.getName());
        assertEquals("john@example.com", authorDTO.getEmail());

        // Verify that the repository method was called
//        verify(authorRepository, times(1)).findAll();

    }

//    @Test
//    public void getAllBooksTest() {
//        //arrange
//        List<Book> expectedBooks = new ArrayList<>() ;
//        expectedBooks.add(new Book(1, "Object oriented programming", 380, 178654));
//        expectedBooks.add(new Book(2, "Programming in C++", 400, 198654));
//
//        when(bookRepository.findAll()).thenReturn(expectedBooks);
//
//        List<Book> actualResponse = service.getAllBooks();
//
//        assertEquals(expectedBooks, actualResponse);
//
//    }

//    @Test
//    public void addBookTest() throws BadRequestException {
//
//        //scenario:1 => adding valid book
//        Book expectedBook = new Book(1, "Object oriented programming", 850, 189761);
//        when(bookRepository.save(expectedBook)).thenReturn(expectedBook);
//        Book actualBook = service.addBook(expectedBook);
//        assertEquals(expectedBook, actualBook);
//
//        //scenario:2 => book with same name
//        Book expedtedBookWithSameName= new Book(2, "Object oriented programming", 760, 174231);
//        when(bookRepository.checkDuplicateBookName(expedtedBookWithSameName.getName())).thenReturn(Optional.of(expectedBook));
//        Exception badRequestExceptionSameBook = assertThrows(BadRequestException.class, ()->service.addBook(expedtedBookWithSameName));
//
//        //scenario:3 => book with same isbn
//        Book expectedBookWithSameIsbn = new Book(3, "Programmin in C++", 390, 189761);
//        when(bookRepository.findByIsbn(expectedBookWithSameIsbn.getIsbn())).thenReturn(Optional.of(expectedBook));
//        Exception badRequestExceptionSameIsbn = assertThrows(BadRequestException.class, ()->service.addBook(expectedBookWithSameIsbn));
//    }

//    @Test
//    public void addAuthorTest() throws BadRequestException
//    {
//        //scenario:1 => adding a valid author
//        Author expectedAuthor = new Author(1, "Anshika", "anshika@gmail.com");
//        when(authorRepository.save(expectedAuthor)).thenReturn(expectedAuthor);
//        Author actualAuthor = service.addAuthor(expectedAuthor);
//        assertEquals(expectedAuthor, actualAuthor);
//
//        //scenario:2 => adding duplicate email id
//        Author expectedAuthorWithDuplicateEmail = new Author(2, "Anshi", "anshika@gmail.com");
//        when(authorRepository.findByEmailId(expectedAuthorWithDuplicateEmail.getEmailId())).thenReturn(Optional.of(expectedAuthor));
//        Exception badRequestException = assertThrows(BadRequestException.class, ()->service.addAuthor(expectedAuthorWithDuplicateEmail));
//    }

//    @Test
//    public void getAuthorByIdTest() throws NotFoundException {
//        // scenario:1 => getting valid author
//        Author author = new Author(1, "Aanchal", "aanchal@gmail.com");
//        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
//
//        Author actualAuthor = service.getAuthorById(author.getId());
//        assertEquals(author, actualAuthor);
//
//        //scenario:2 => author with authorId not found
//        Exception notFound = assertThrows(NotFoundException.class, ()->service.getAuthorById(2));
//    }

//    @Test
//    public void getBookByAuthorIdTest() throws NotFoundException {
//        //scenario:1
//        Author expectedAuthor = new Author(1, "Aanchal", "aanchal@gmail.com");
//        Book expectedBook = new Book(1, "Object oriented programming", 850, 189761);
//        List<Book> expectedBookList = new ArrayList<>();
//        expectedBookList.add(expectedBook);
//
//        List<Book_Author> expectedBookAuthor = new ArrayList<>();
//        expectedBookAuthor.add(new Book_Author(expectedBook, expectedAuthor));
//
//        when(authorRepository.findById(expectedAuthor.getId())).thenReturn(Optional.of(expectedAuthor));
//
//        when(bookAuthorRepository.findAll()).thenReturn(expectedBookAuthor);
//
//        when(bookRepository.findById(expectedBook.getId())).thenReturn(Optional.of(expectedBook));
//
//        List<Book> actualBooksById = service.getBookByAuthorId(expectedAuthor.getId());
//
//        assertEquals(expectedBookList, actualBooksById);
//
//        //scenario 2 : => Not found
//        NotFoundException notFound = assertThrows(NotFoundException.class, ()->service.getBookByAuthorId(2));
//
//
//    }

}