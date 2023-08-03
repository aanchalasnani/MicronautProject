package com.example.service.impl;

import com.example.entity.Author;
import com.example.entity.Book;
import com.example.entity.Book_Author;
import com.example.exception.BadRequestException;
import com.example.exception.NotFoundException;
import com.example.model.dto.AuthorDTO;
import com.example.model.dto.BookAuthorDTO;
import com.example.model.dto.BookDTO;
import com.example.model.response.AuthorResponse;
import com.example.model.response.BookResponse;
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

    @Override
    public BookResponse addBook(BookDTO bookDTO) throws BadRequestException {
        validateBook(bookDTO);

        Book book = new Book();
        book.setName(bookDTO.getName());
        book.setIsbn(bookDTO.getIsbn());
        book.setPrice(bookDTO.getPrice());

        Book savedBook = bookRepository.save(book);
        return BookResponse.builder().bookId(savedBook.getId()).status("SuccessFully Added").build();

    }

    private void validateBook(BookDTO bookDTO) throws BadRequestException {
        Optional<Book> queryBook =bookRepository.checkDuplicateBookName(bookDTO.getName());

        if(queryBook.isPresent()){
            throw new BadRequestException("Bad Request Exception", "Book with name " + bookDTO.getName() + " already exists");
        }
    }

    @Override
    public BookResponse addBookWithAuthorList(BookDTO bookDTO, List<AuthorDTO> authorDTOList) throws BadRequestException{
        validateBook(bookDTO);

        Book book = this.mapBookDTOToBook(bookDTO);
        Book savedBook = bookRepository.save(book);

        for(AuthorDTO authorDTO : authorDTOList) {
            Author author = this.mapAuthorDTOToAuthor(authorDTO);
            Author savedAuthor = authorRepository.save(author);

            bookAuthorRepository.save(new Book_Author(savedBook, savedAuthor));
        }
        return BookResponse.builder()
                .status("Successfully Added Book")
                .bookId(savedBook.getId()).build();
    }

    @Override
    public BookAuthorDTO addBookAuthor(int authorId, int bookId ) throws NotFoundException{
        Book_Author bookAuthor = validateBookAuthor(authorId, bookId);
        Book_Author savedBookAuthor = bookAuthorRepository.save(bookAuthor);
        BookAuthorDTO bookAuthorDTO = mapBookAuthorToDTO(savedBookAuthor);
        return bookAuthorDTO;
    }

    Book_Author validateBookAuthor(int authorId, int bookId) throws NotFoundException {
        Optional<Book> book = bookRepository.findById(bookId);
        if(book.isEmpty()){
            throw new NotFoundException("Not found", "Book with id : "+bookId+" does not exist" );
        }
        Optional<Author> author = authorRepository.findById(authorId);
        if(author.isEmpty()){
            throw new NotFoundException("Not found", "Author with id : "+authorId+" does not exist" );

        }
        return new Book_Author(book.get(), author.get());
    }

    @Override
    public AuthorResponse addAuthor(AuthorDTO authorDTO) throws BadRequestException {
        validateAuthor(authorDTO);
        Author author = mapAuthorDTOToAuthor(authorDTO);
        Author savedAuthor = authorRepository.save(author);
        return AuthorResponse.builder().authorId(savedAuthor.getId()).status("Author SuccessFully Added.").build();
    }

    private void validateAuthor(AuthorDTO authorDTO) throws BadRequestException {
        Optional<Author> existingAuthor = authorRepository.findByEmailId(authorDTO.getEmail());
        if (existingAuthor.isPresent()) {
            throw new BadRequestException("Bad Request Exception","Author with email " + authorDTO.getEmail() + " already exists");
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
        List<Book_Author> bookAuthorList = bookAuthorRepository.findAll();
        for(Book_Author bookAuthor : bookAuthorList) {
            if(bookAuthor.getAuthor().getId() == authorId) {
                Book authorBook = bookRepository.findById(bookAuthor.getBook().getId()).orElse(null);
                BookDTO bookDTO = mapBookTOBookDTO(authorBook);
                bookList.add(bookDTO);
            }
        }
        return bookList;
    }

    @Override
    public AuthorResponse addAuthorWithBookList(AuthorDTO authorDTO, List<BookDTO> bookDTOList) throws BadRequestException {
        validateAuthor(authorDTO);
        Author author = mapAuthorDTOToAuthor(authorDTO);
        Author savedAuthor =  authorRepository.save(author);

        for(BookDTO bookDTO : bookDTOList) {
            Book book = this.mapBookDTOToBook(bookDTO);
            Book savedBook = bookRepository.save(book);

            bookAuthorRepository.save(new Book_Author(savedBook, savedAuthor));
        }
        return AuthorResponse.builder()
                .status("Successfully Added Author")
                .authorId(savedAuthor.getId()).build();

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
