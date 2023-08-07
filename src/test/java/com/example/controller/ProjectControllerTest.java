//package com.example.controller;
//
//
//import com.example.entity.Author;
//import com.example.repository.AuthorRepository;
//import com.example.repository.BookAuthorRepository;
//import com.example.repository.BookRepository;
//import com.example.service.ProjectService;
//import io.micronaut.http.HttpResponse;
//import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@MicronautTest
////@ExtendWith(MockitoExtension.class)
//public class ProjectControllerTest {
//
//    @Mock
//    AuthorRepository authorRepository;
//    @Mock
//    BookRepository bookRepository;
//
//    @Mock
//    BookAuthorRepository bookAuthorRepository;
//
//
//    @Test
//    public void testGetAllAuthors() {
//        List<Author> authors = new ArrayList<>();
//        authors.add(new Author(1, "John Doe", "john.doe@example.com"));
//        authors.add(new Author(2, "Jane Smith", "jane.smith@example.com"));
//
//        ProjectService service = new ProjectService(authorRepository, bookRepository, bookAuthorRepository) {
//            @Override
//            public List<Author> getAllAuthors() {
//                return authors;
//            }
//        };
//
//        ProjectController controller = new ProjectController(service);
//        HttpResponse<List<Author>> response = controller.getAllAuthors();
//        assertEquals(200, response.getStatus().getCode());
//        assertEquals(authors, response.body());
//    }
//
//    @Test
//    public void testWelcomeAPI() {
//        String msg = "WELCOME TO A MICRONAUT PROJECT FOR BEGINNERS";
//        ProjectService service = new ProjectService(authorRepository, bookRepository, bookAuthorRepository);
//        ProjectController controller = new ProjectController(service);
//        String response = controller.getHello();
//        assertEquals(msg, response);
//    }
//}
