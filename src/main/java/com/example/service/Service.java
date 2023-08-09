package com.example.service;

import com.example.entity.Author;
import com.example.exception.NotFoundException;
import com.example.model.dto.AuthorDTO;
import com.example.model.dto.BookAuthorDTO;
import com.example.model.dto.BookDTO;
import com.example.model.request.AddAuthorRequest;
import com.example.model.request.AddBookAuthorRequest;
import com.example.model.request.AddBookRequest;
import com.example.model.response.AddAuthorResponse;
import com.example.model.response.AddBookResponse;

import java.util.List;

public interface Service {
    List<AuthorDTO> getAllAuthors();
    List<BookDTO> getAllBooks();
    BookAuthorDTO addBookAuthor(AddBookAuthorRequest addBookAuthorRequest) throws NotFoundException;
    Author getAuthorById(int authorId) throws NotFoundException;
    List<BookDTO> getBookByAuthorId(int authorId)  throws NotFoundException;
    AddAuthorResponse addAuthor(AddAuthorRequest addAuthorRequest) ;
    AddBookResponse addBook(AddBookRequest addBookRequest) throws  NotFoundException;
}
