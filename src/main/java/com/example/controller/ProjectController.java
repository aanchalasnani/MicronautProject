package com.example.controller;

import com.example.entity.Author;
import com.example.exception.NotFoundException;
import com.example.model.dto.AuthorDTO;
import com.example.model.dto.BookAuthorDTO;
import com.example.model.dto.BookDTO;
import com.example.model.request.AddBookAuthorRequest;
import com.example.model.request.AddAuthorRequest;
import com.example.model.request.AddBookRequest;
import com.example.model.response.AddAuthorResponse;
import com.example.service.Service;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

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
    public HttpResponse<List<AuthorDTO>> getAllAuthors() {
        return HttpResponse.ok(service.getAllAuthors());
    }

    @Get("/books")
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<List<BookDTO>> getAllBooks() {
        return HttpResponse.ok(service.getAllBooks());
    }

    @Get("/authors/{authorId}")
    public HttpResponse<Author> getAuthorById(@PathVariable int authorId) {
        try {
            return HttpResponse.ok(service.getAuthorById(authorId));
        }
        catch(NotFoundException ex) {
            return  HttpResponse.status(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @Post("/authors")
    @Consumes("application/json; charset=utf-8")
    @Produces("application/json; charset=utf-8")
    public HttpResponse<AddAuthorResponse> addAuthor(@Body AddAuthorRequest addAuthorRequest) {
        return HttpResponse.ok(service.addAuthor(addAuthorRequest));
    }

    @Post("/books")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public HttpResponse addBook(@Body AddBookRequest addBookRequest) {
        try {
            return HttpResponse.ok(service.addBook(addBookRequest));
        }
        catch (NotFoundException ex) {
            return HttpResponse.status(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @Get("/authors/{authorId}/books")
    @Consumes("application/json; charset=utf-8")
    @Produces("application/json; charset=utf-8")
    public HttpResponse<List<BookDTO>> getBooksByAuthorId(@PathVariable int authorId) {
        try {
            return HttpResponse.ok(service.getBookByAuthorId(authorId));
        } catch (NotFoundException ex) {
            return HttpResponse.status(HttpStatus.NOT_FOUND,ex.getMessage());
        }
    }

    @Post("/books/author")
    @Consumes("application/json; charset=utf-8")
    @Produces("application/json; charset=utf-8")
    public HttpResponse<BookAuthorDTO> addBookAuthor(@Body AddBookAuthorRequest addBookAuthorRequest) {
        try {
            return HttpResponse.ok(service.addBookAuthor(addBookAuthorRequest));
        }
        catch(NotFoundException ex) {
            return HttpResponse.status(HttpStatus.NOT_FOUND,ex.getMessage());
        }
    }
}
