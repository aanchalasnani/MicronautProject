package com.example.service;

import com.example.entity.Author;
import com.example.entity.Book_Author;
import com.example.exception.BadRequestException;
import com.example.exception.NotFoundException;
import com.example.model.dto.AuthorDTO;
import com.example.model.dto.BookDTO;
import com.example.model.response.AuthorResponse;
import com.example.model.response.BookResponse;

import java.util.List;

public interface Service {
    List<AuthorDTO> getAllAuthors();
    List<BookDTO> getAllBooks();
    BookResponse addBook(BookDTO bookDTO) throws BadRequestException;
    BookResponse addBookWithAuthorList(BookDTO bookDTO, List<AuthorDTO> authorDTOList) throws BadRequestException;
    Book_Author addBookAuthor(Book_Author bookAuthor);
    AuthorResponse addAuthor(AuthorDTO authorDTO) throws BadRequestException;
    Author getAuthorById(int authorId) throws NotFoundException;
    List<BookDTO> getBookByAuthorId(int authorId)  throws NotFoundException;
    AuthorResponse addAuthorWithBookList(AuthorDTO authorDTO, List<BookDTO> bookDTOList) throws BadRequestException;
}
