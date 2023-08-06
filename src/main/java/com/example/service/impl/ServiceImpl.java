package com.example.service.impl;

import com.example.entity.Author;
import com.example.entity.Book;
import com.example.entity.Book_Author;
import com.example.exception.NotFoundException;
import com.example.model.dto.AuthorDTO;
import com.example.model.dto.BookAuthorDTO;
import com.example.model.dto.BookDTO;
import com.example.model.request.CreateAuthorRequest;
import com.example.model.request.CreateBookRequest;
import com.example.model.response.CreateAuthorResponse;
import com.example.model.response.CreateBookResponse;
import com.example.repository.AuthorRepository;
import com.example.repository.BookAuthorRepository;
import com.example.repository.BookRepository;
import com.example.service.Service;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class ServiceImpl implements Service {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final BookAuthorRepository bookAuthorRepository;

    @Inject
    ServiceImpl(AuthorRepository authorRepository, BookRepository bookRepository, BookAuthorRepository bookAuthorRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.bookAuthorRepository = bookAuthorRepository;
    }


    @Override
    public List<AuthorDTO> getAllAuthors() {
        List<AuthorDTO> authorsDTO = new ArrayList<>();
        List<Author> authors = authorRepository.findAll();
        for(Author author : authors) {
            AuthorDTO authorDTO = this.mapAuthorToAuthorDTO(author);
            authorsDTO.add(authorDTO);
        }
        return authorsDTO;
    }

    @Override
    public List<BookDTO> getAllBooks() {
        List<BookDTO> booksDTO = new ArrayList<>();
        List<Book> books = bookRepository.findAll();
        for(Book book : books) {
            BookDTO bookDTO = this.mapBookTOBookDTO(book);
            booksDTO.add(bookDTO);
        }
        return booksDTO;
    }

    public CreateBookResponse addBook(BookDTO bookDTO) {
        Book book = mapBookDTOToBook(bookDTO);
        Book savedBook = bookRepository.save(book);
        return CreateBookResponse.builder().bookId(savedBook.getId()).status("SuccessFully Added Book").build();
    }

    public CreateBookResponse addBookWithAuthors(BookDTO bookDTO, List<Integer> authorIds) throws NotFoundException{
        Book book = this.mapBookDTOToBook(bookDTO);
        Book savedBook = bookRepository.save(book);

        for(Integer authorId : authorIds) {
            validateAuthor(authorId);
            Optional<Author> existingAuthor = authorRepository.findById(authorId);
            bookAuthorRepository.save(new Book_Author(savedBook, existingAuthor.get()));
        }
        return CreateBookResponse.builder()
                .status("Successfully Added Book")
                .bookId(savedBook.getId()).build();
    }

    void validateAuthor(int authorId) throws NotFoundException{
        if(authorRepository.findById(authorId).isEmpty())
        {
            throw new NotFoundException("Not found", "Author with authorId : "+ authorId +" does not exist");
        }
    }

    @Override
    public BookAuthorDTO addBookAuthor(int authorId, int bookId ) throws NotFoundException{
        validateBookAuthor(authorId, bookId);
        Book_Author bookAuthor = new Book_Author(bookRepository.findById(bookId).orElse(null), authorRepository.findById(authorId).orElse(null));
        Book_Author savedBookAuthor = bookAuthorRepository.save(bookAuthor);
        BookAuthorDTO bookAuthorDTO = mapBookAuthorToDTO(savedBookAuthor);
        return bookAuthorDTO;
    }

    void validateBookAuthor(int authorId, int bookId) throws NotFoundException {
        if(bookRepository.findById(bookId).isEmpty()){
            throw new NotFoundException("Not found", "Book with id : "+bookId+" does not exist" );
        }
        if(authorRepository.findById(authorId).isEmpty()){
            throw new NotFoundException("Not found", "Author with id : "+authorId+" does not exist" );

        }
    }

    public CreateAuthorResponse addAuthor(AuthorDTO authorDTO) {
        Author author = mapAuthorDTOToAuthor(authorDTO);
        Author savedAuthor = authorRepository.save(author);
        return CreateAuthorResponse.builder().authorId(savedAuthor.getId()).status("Author SuccessFully Added.").build();
    }

    public CreateAuthorResponse addAuthorWithBooks(AuthorDTO authorDTO, List<Integer> bookIds) throws NotFoundException{
        Author author = this.mapAuthorDTOToAuthor(authorDTO);
        Author savedAuthor = authorRepository.save(author);

        for(Integer bookId : bookIds) {
            validateBook(bookId);
            Optional<Book> existingBook = bookRepository.findById(bookId);
            bookAuthorRepository.save(new Book_Author(existingBook.get(), savedAuthor));
        }
        return CreateAuthorResponse.builder()
                .status("Successfully Added Author")
                .authorId(savedAuthor.getId())
                .build();
    }

    void validateBook(int bookId) throws NotFoundException{
        if(bookRepository.findById(bookId).isEmpty()) throw new NotFoundException("Not found", "Book with BookId : "+ bookId+" does not exist");
    }

    @Override
    public CreateAuthorResponse createAuthor(CreateAuthorRequest createAuthorRequest) throws NotFoundException{
        AuthorDTO authorDTO = new AuthorDTO(createAuthorRequest.getName(), createAuthorRequest.getEmailId());
        if(createAuthorRequest.getBookIds()!=null) {
            return addAuthorWithBooks(authorDTO, createAuthorRequest.getBookIds());
        }else {
            return addAuthor(authorDTO);
        }
    }

    @Override
    public CreateBookResponse createBook(CreateBookRequest createBookRequest) throws NotFoundException {
        BookDTO bookDTO = new BookDTO(createBookRequest.getName(), createBookRequest.getIsbn(), createBookRequest.getPrice());
        if(createBookRequest.getAuthorIds()!=null) {
            return addBookWithAuthors(bookDTO, createBookRequest.getAuthorIds());
        }else {
            return addBook(bookDTO);
        }
    }


    @Override
    public Author getAuthorById(int authorId) throws NotFoundException {
        validateAuthorById(authorId);
        return authorRepository.findById(authorId).orElse(null);
    }

    private void validateAuthorById(int authorId) throws NotFoundException {
        if(authorRepository.findById(authorId).isEmpty()){
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

    private BookDTO mapBookTOBookDTO(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setName(book.getName());
        bookDTO.setIsbn(book.getIsbn());
        bookDTO.setId(book.getId());
        bookDTO.setPrice(book.getPrice());
        return bookDTO;
    }

    private Author mapAuthorDTOToAuthor(AuthorDTO authorDTO) {
        Author author = new Author();
        author.setName(authorDTO.getName());
        author.setEmailId(authorDTO.getEmail());
        return author;
    }

    private Book mapBookDTOToBook(BookDTO bookDTO) {
        Book book = new Book();
        book.setName(bookDTO.getName());
        book.setIsbn(bookDTO.getIsbn());
        book.setPrice(bookDTO.getIsbn());
        return book;
    }

    private AuthorDTO mapAuthorToAuthorDTO(Author author) {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(author.getId());
        authorDTO.setName(author.getName());
        authorDTO.setEmail(author.getEmailId());
        return authorDTO;
    }

    private BookAuthorDTO mapBookAuthorToDTO(Book_Author bookAuthor){
        BookAuthorDTO bookAuthorDTO = new BookAuthorDTO();
        bookAuthorDTO.setAuthor(bookAuthor.getAuthor());
        bookAuthorDTO.setBook(bookAuthor.getBook());
        bookAuthorDTO.setId(bookAuthor.getId());
        return bookAuthorDTO;
    }

}
