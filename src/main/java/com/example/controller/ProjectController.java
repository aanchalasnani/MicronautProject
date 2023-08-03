package com.example.controller;

import com.example.exception.BadRequestException;
import com.example.exception.NotFoundException;
import com.example.model.dto.AuthorDTO;
import com.example.model.dto.BookDTO;
import com.example.model.request.AuthorRequest;
import com.example.model.request.BookAuthorRequest;
import com.example.model.request.BookRequest;
import com.example.model.response.AuthorResponse;
import com.example.model.response.BookResponse;
import com.example.response.ErrorResponse;
import com.example.service.Service;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class ProjectController {

    private final Service service;

    @Inject
    ProjectController(Service service) {
        this.service = service;
    }

    @Get("/")
    public String getHello() {
        return "WELCOME TO A MICRONAUT PROJECT FOR BEGINNERS";
    }

    @Get("/authors")
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse getAllAuthors() {
        return HttpResponse.ok(service.getAllAuthors());
    }

    @Get("/books")
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse getAllBooks() {
        return HttpResponse.ok(service.getAllBooks());
    }

    @Get("/authors/{author-id}")
    public HttpResponse getAuthorByid( @PathVariable int authorId) {
        try {
            return HttpResponse.ok(service.getAuthorById(authorId));
        }
        catch(NotFoundException ex) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.getCode(), ex.getMessage());
            return  HttpResponse.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }


    @Post("/authors")
    @Consumes("application/json; charset=utf-8")
    @Produces("application/json; charset=utf-8")
    public HttpResponse addAuthor(@Body AuthorRequest authorRequest) {
        AuthorDTO authorDTO = new AuthorDTO(authorRequest.getName(), authorRequest.getEmailId());
        try {
            if(authorRequest.getBookList().isPresent()){

                List<BookDTO> bookDTOList = new ArrayList<>();
                for (BookRequest bookRequest : authorRequest.getBookList().get()) {
                    BookDTO bookDTO = new BookDTO(bookRequest.getName(), bookRequest.getIsbn(), bookRequest.getPrice());
                    bookDTOList.add(bookDTO);
                }
                return HttpResponse.ok(service.addAuthorWithBookList(authorDTO, bookDTOList));
            }
            else {
                AuthorResponse savedAuthor = service.addAuthor(authorDTO);
                return HttpResponse.ok(savedAuthor);
            }

        }
        catch(BadRequestException ex) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.getCode(), ex.getMessage());
            return HttpResponse.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Post("/books")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public HttpResponse addBook(@Body BookRequest bookRequest){
        BookDTO bookDTO = new BookDTO(bookRequest.getName(), bookRequest.getIsbn(), bookRequest.getPrice());
        try {
            if(bookRequest.getAuthorList().isPresent()) {

                List<AuthorDTO> authorListDTO= new ArrayList<>();

                for (AuthorRequest authorRequest : bookRequest.getAuthorList().get()) {
                    AuthorDTO authorDTO = new AuthorDTO(authorRequest.getName(), authorRequest.getEmailId());
                    authorListDTO.add(authorDTO);
                }

                return HttpResponse.ok(service.addBookWithAuthorList(bookDTO, authorListDTO));
            }
            else {
                BookResponse savedBook = service.addBook(bookDTO);
                return HttpResponse.ok(savedBook);
            }
        }
        catch(BadRequestException ex){
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.getCode(), ex.getMessage());
            return HttpResponse.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Get("/authors/{authorId}/books")
    @Consumes("application/json; charset=utf-8")
    @Produces("application/json; charset=utf-8")
    public HttpResponse getBooksByAuthorId(@PathVariable int authorId) {
        try {
            return HttpResponse.ok(service.getBookByAuthorId(authorId));
        } catch (NotFoundException ex) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.getCode(), ex.getMessage());
            return HttpResponse.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @Post("/books/author")
    @Consumes("application/json; charset=utf-8")
    @Produces("application/json; charset=utf-8")
    public HttpResponse addBookAuthor(@Body BookAuthorRequest bookAuthorRequest) {
        try {
            return HttpResponse.ok(service.addBookAuthor(bookAuthorRequest.getAuthorId(), bookAuthorRequest.getBookId()));
        }
        catch(NotFoundException ex) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.getCode(), ex.getMessage());
            return HttpResponse.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}
