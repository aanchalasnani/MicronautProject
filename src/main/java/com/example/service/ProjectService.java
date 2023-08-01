package com.example.service;

import com.example.entity.Author;
import com.example.entity.Book;
import com.example.entity.Book_Author;
import com.example.exception.BadRequestException;
import com.example.exception.NotFoundException;
import com.example.model.dto.BookDTO;
import com.example.repository.AuthorRepository;
import com.example.repository.BookAuthorRepository;
import com.example.repository.BookRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class ProjectService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final BookAuthorRepository bookAuthorRepository;

    @Inject
    public ProjectService(AuthorRepository authorRepository, BookRepository bookRepository, BookAuthorRepository bookAuthorRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.bookAuthorRepository = bookAuthorRepository;
    }


    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book addBook(BookDTO bookDTO) throws BadRequestException {
        Optional<Book> queryBook =bookRepository.checkDuplicateBookName(bookDTO.getName());

        if(queryBook.isPresent()){
            throw new BadRequestException("Bad Request Exception", "Book with name " + bookDTO.getName() + " already exists");
        }

        Book book = new Book();
        book.setName(bookDTO.getName());
        book.setIsbn(bookDTO.getIsbn());
        book.setPrice(bookDTO.getPrice());

        return bookRepository.save(book);
    }

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
        List<Book_Author> bookAuthorList = bookAuthorRepository.findAll();
        for(Book_Author bookAuthor : bookAuthorList) {
            if(bookAuthor.getAuthor().getId() == authorId) {
                Book authorBook = bookRepository.findById(bookAuthor.getBook().getId()).orElse(null);
                bookList.add(authorBook);
            }
        }
        return bookList;
    }


}
