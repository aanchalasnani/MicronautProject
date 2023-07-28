package com.example.service;

import com.example.entity.Author;
import com.example.entity.Book;
import com.example.entity.Book_Author;
import com.example.exception.BadRequestException;
import com.example.exception.NotFoundException;
import com.example.repository.AuthorRepository;
import com.example.repository.BookAuthorRepository;
import com.example.repository.BookRepository;
import io.micronaut.inject.validation.RequiresValidation;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class ProjectService {

    @Inject
    AuthorRepository authorRepository;

    @Inject
    BookRepository bookRepository;

    @Inject
    BookAuthorRepository bookAuthorRepository;

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @RequiresValidation
    public Book addBook(Book book) throws BadRequestException {
        Optional<Book> QueryBook =bookRepository.checkDuplicateBookName(book.getName());
        if(QueryBook.isPresent()){
            throw new BadRequestException("Bad Request Exception", "Book with name " + book.getName() + " already exists");
        }
        Optional<Book> DuplicateIsbnBookQuery = bookRepository.findByIsbn(book.getIsbn());
        if(DuplicateIsbnBookQuery.isPresent()){
            throw new BadRequestException("Bad Request Exception", "Book with isbn " + book.getIsbn() + " already exists");
        }
        return bookRepository.save(book);
    }

    @RequiresValidation
    public Author addAuthor(Author author) throws BadRequestException {
        Optional<Author> existingAuthor = authorRepository.findByEmailId(author.getEmailId());
        if (existingAuthor.isPresent()) {
            throw new BadRequestException("Bad Request Exception","Author with email " + author.getEmailId() + " already exists");
        }
        return authorRepository.save(author);
    }

    public Author getAuthorById(int authorId) throws NotFoundException {
        if(authorRepository.findById(authorId).isEmpty()){
            throw new NotFoundException("Not Found Exception", "Author with authorId : " + authorId + " does not exist.");
        }
        return authorRepository.findById(authorId).orElse(null);
    }

    public List<Book> getBookByAuthorId(int authorId)  throws NotFoundException {

        List<Book> bookList = new ArrayList<>();
        if(authorRepository.findById(authorId).isEmpty()){
            throw new NotFoundException("Not Found Exception", "Author with authorId : " + authorId + " does not exist.");
        }
        List<Book_Author> BookAuthorList = bookAuthorRepository.findAll();
        for(Book_Author bookAuthor : BookAuthorList) {
            if(bookAuthor.getAuthor().getId() == authorId) {
                Book authorBook = bookRepository.findById(bookAuthor.getBook().getId()).orElse(null);
                bookList.add(authorBook);
            }
        }
        return bookList;
    }


}
