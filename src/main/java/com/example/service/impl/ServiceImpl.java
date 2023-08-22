package com.example.service.impl;

import com.example.entity.Author;
import com.example.entity.Book;
import com.example.entity.Book_Author;
import com.example.exception.NotFoundException;
import com.example.model.dto.AuthorDTO;
import com.example.model.dto.BookAuthorDTO;
import com.example.model.dto.BookDTO;
import com.example.model.request.AddAuthorRequest;
import com.example.model.request.AddBookAuthorRequest;
import com.example.model.request.AddBookRequest;
import com.example.model.response.AddAuthorResponse;
import com.example.model.response.AddBookResponse;
import com.example.repository.AuthorRepository;
import com.example.repository.BookAuthorRepository;
import com.example.repository.BookRepository;
import com.example.service.Service;
import com.example.service.clients.RedisCacheClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class ServiceImpl implements Service {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final BookAuthorRepository bookAuthorRepository;
    private final RedisCacheClient redisCacheClient;

    @Inject
    public ServiceImpl(AuthorRepository authorRepository, BookRepository bookRepository, BookAuthorRepository bookAuthorRepository, RedisCacheClient redisCacheClient) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.bookAuthorRepository = bookAuthorRepository;
        this.redisCacheClient = redisCacheClient;
    }


    @Override
    public List<AuthorDTO> getAllAuthors() {
//        List<AuthorDTO> authorsDTO = new ArrayList<>();
//        List<Author> authors = authorRepository.findAll();
//        for(Author author : authors) {
//            AuthorDTO authorDTO = mapAuthorToAuthorDTO(author);
//            authorsDTO.add(authorDTO);
//        }
//        return authorsDTO;
        List<AuthorDTO> authorsDTO = new ArrayList<>();
        if (redisCacheClient.exists("allAuthors")) {
            String cachedData = redisCacheClient.get("allAuthors");
            authorsDTO = convertJsonToAuthorDTOList(cachedData);
        } else {
            List<Author> authors = authorRepository.findAll();
            for (Author author : authors) {
                AuthorDTO authorDTO = mapAuthorToAuthorDTO(author);
                authorsDTO.add(authorDTO);
            }
            // Cache the data
            redisCacheClient.set("allAuthors", convertAuthorDTOListToJson(authorsDTO));
        }
        return authorsDTO;
    }

    @Override
    public List<BookDTO> getAllBooks() {
        List<BookDTO> booksDTO = new ArrayList<>();
        List<Book> books = bookRepository.findAll();
        for(Book book : books) {
            BookDTO bookDTO = mapBookTOBookDTO(book);
            booksDTO.add(bookDTO);
        }
        return booksDTO;
    }

    private AddBookResponse addABook(BookDTO bookDTO) {
        Book savedBook = saveBook(bookDTO);
        return AddBookResponse.builder()
                .bookId(savedBook.getId())
                .status("Success")
                .build();
    }

    @Transactional
    public AddBookResponse addBookByAuthorIds(BookDTO bookDTO, List<Integer> authorIds) throws NotFoundException{
        Book savedBook = saveBook(bookDTO);

        for(Integer authorId : authorIds) {
            Author existingAuthor = fetchAuthorById(authorId);
            bookAuthorRepository.save(new Book_Author(savedBook, existingAuthor));
        }
        return AddBookResponse.builder()
                .status("Success")
                .bookId(savedBook.getId())
                .build();
    }

    private Author fetchAuthorById(int authorId) throws NotFoundException {
        Optional<Author> author = authorRepository.findById(authorId);
        if(author.isEmpty())
        {
            throw new NotFoundException("Not found", "Author with authorId : "+ authorId +" does not exist");
        }
        return author.get();
    }

    private Book saveBook(BookDTO bookDTO) {
        Book book = mapBookDTOToBook(bookDTO);
        return bookRepository.save(book);
    }

    @Override
    public BookAuthorDTO addBookAuthor(AddBookAuthorRequest addBookAuthorRequest) throws NotFoundException{
        int authorId = addBookAuthorRequest.getAuthorId();
        int bookId = addBookAuthorRequest.getBookId();
        validateBookAuthor(authorId, bookId);
        Book book = bookRepository.findById(bookId).get();
        Author author = authorRepository.findById(authorId).get();
        Book_Author bookAuthor = new Book_Author(book,author);
        Book_Author savedBookAuthor = bookAuthorRepository.save(bookAuthor);
        return mapBookAuthorToDTO(savedBookAuthor);
    }

    private void validateBookAuthor(int authorId, int bookId) throws NotFoundException {
        Optional<Book> book = bookRepository.findById(bookId);
        if(book.isEmpty()){
            throw new NotFoundException("Not found", "Book with id : "+bookId+" does not exist" );
        }
        Optional<Author> author = authorRepository.findById(authorId);
        if(author.isEmpty()){
            throw new NotFoundException("Not found", "Author with id : "+authorId+" does not exist" );

        }
    }

    private AddAuthorResponse addAnAuthor(AuthorDTO authorDTO) {
        Author author = mapAuthorDTOToAuthor(authorDTO);
        Author savedAuthor = authorRepository.save(author);
        return AddAuthorResponse.builder()
                .authorId(savedAuthor.getId())
                .status("Success")
                .build();
    }

    @Override
    public AddAuthorResponse addAuthor(AddAuthorRequest addAuthorRequest){
        AuthorDTO authorDTO = AuthorDTO.builder()
                .name(addAuthorRequest.getName())
                .email(addAuthorRequest.getEmailId())
                .build();
        return addAnAuthor(authorDTO);
    }

    @Override
    public AddBookResponse addBook(AddBookRequest addBookRequest) throws NotFoundException {
        BookDTO bookDTO = BookDTO.builder()
                .name(addBookRequest.getName())
                .isbn(addBookRequest.getIsbn())
                .price(addBookRequest.getPrice())
                .build();
        if(addBookRequest.getAuthorIds()!=null) {
            return addBookByAuthorIds(bookDTO, addBookRequest.getAuthorIds());
        }else {
            return addABook(bookDTO);
        }
    }


    @Override
    public Author getAuthorById(int authorId) throws NotFoundException {
        validateAuthorById(authorId);
        return authorRepository.findById(authorId).get();
    }

    private void validateAuthorById(int authorId) throws NotFoundException {
        Optional<Author> author = authorRepository.findById(authorId);
        if(author.isEmpty()){
            throw new NotFoundException("Not Found Exception", "Author with authorId : " + authorId + " does not exist.");
        }
    }

    @Override
    public List<BookDTO> getBookByAuthorId(int authorId)  throws NotFoundException {

        List<BookDTO> bookList = new ArrayList<>();
        validateAuthorById(authorId);
        List<Integer> bookIds = bookAuthorRepository.findBookAuthorMapping(authorId);
        for(Integer bookId : bookIds) {
                Book book = bookRepository.findById(bookId).orElse(null);
                BookDTO bookDTO = mapBookTOBookDTO(book);
                bookList.add(bookDTO);
        }
        return bookList;
    }

    private List<AuthorDTO> convertJsonToAuthorDTOList(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, new TypeReference<List<AuthorDTO>>() {});
        } catch (Exception e) {
            // Handle exceptions appropriately
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private String convertAuthorDTOListToJson(List<AuthorDTO> authorsDTO) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(authorsDTO);
        } catch (Exception e) {
            // Handle exceptions appropriately
            e.printStackTrace();
            return "";
        }
    }

    private BookDTO mapBookTOBookDTO(Book book) {
        return BookDTO.builder()
                .id(book.getId())
                .name(book.getName())
                .isbn(book.getIsbn())
                .price(book.getPrice())
                .build();
    }

    private Author mapAuthorDTOToAuthor(AuthorDTO authorDTO) {
        return Author.builder()
                .name(authorDTO.getName())
                .emailId(authorDTO.getEmail())
                .build();
    }

    private Book mapBookDTOToBook(BookDTO bookDTO) {
        return Book.builder()
                .name(bookDTO.getName())
                .isbn(bookDTO.getIsbn())
                .price(bookDTO.getPrice())
                .build();
    }

    private AuthorDTO mapAuthorToAuthorDTO(Author author) {
        return AuthorDTO.builder()
                .id(author.getId())
                .name(author.getName())
                .email(author.getEmailId())
                .build();
    }

    private BookAuthorDTO mapBookAuthorToDTO(Book_Author bookAuthor){
        return BookAuthorDTO.builder()
                .author(bookAuthor.getAuthor())
                .book(bookAuthor.getBook())
                .build();
    }

}
