package com.example.controller;

import com.example.entity.Author;
import com.example.entity.Book;
import com.example.exception.BadRequestException;
import com.example.exception.NotFoundException;
import com.example.response.ErrorResponse;
import com.example.service.ProjectService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.inject.validation.RequiresValidation;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Controller
@Slf4j
public class ProjectController {

    @Inject
    ProjectService service;

    @Get("/")
    public String getHello() {
        return "WELCOME TO A MICRONAUT PROJECT FOR BEGINNERS";
    }

    @Get("/authors")
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<List<Author>> getAllAuthors() {
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

    /*
        This method is used to add an author to the author table
    */
    @Post("/authors")
    @Consumes("application/json; charset=utf-8")
    @Produces("application/json; charset=utf-8")
    public HttpResponse addAuthor(@Body Author author) {
        // Disable serialization for the Author entity class using @Serdeable
        try {
            Author savedAuthor = service.addAuthor(author);
            return HttpResponse.ok(savedAuthor);
        }
        catch(BadRequestException ex) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.getCode(), ex.getMessage());
            return HttpResponse.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Post("/books")
    @Consumes("application/json; charset=utf-8")
    @Produces("application/json; charset=utf-8")
    @RequiresValidation
    public HttpResponse addBook(@Body  Book book) {
        try {
            Book savedBook = service.addBook(book);
            return HttpResponse.ok(savedBook);
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
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.getCode(), ex.getMessage());
            return HttpResponse.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}
