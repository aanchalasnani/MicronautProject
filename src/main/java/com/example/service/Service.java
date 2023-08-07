package com.example.service;

import com.example.entity.Author;
import com.example.exception.NotFoundException;
import com.example.model.dto.AuthorDTO;
import com.example.model.dto.BookAuthorDTO;
import com.example.model.dto.BookDTO;
import com.example.model.request.CreateAuthorRequest;
import com.example.model.request.CreateBookRequest;
import com.example.model.response.CreateAuthorResponse;
import com.example.model.response.CreateBookResponse;

import java.util.List;

public interface Service {
    List<AuthorDTO> getAllAuthors();
    List<BookDTO> getAllBooks();
    BookAuthorDTO addBookAuthor(int authorId, int bookId) throws NotFoundException;
    Author getAuthorById(int authorId) throws NotFoundException;
    List<BookDTO> getBookByAuthorId(int authorId)  throws NotFoundException;
    CreateAuthorResponse createAuthor(CreateAuthorRequest createAuthorRequest) throws NotFoundException;
    CreateBookResponse createBook(CreateBookRequest createBookRequest) throws  NotFoundException;
}
