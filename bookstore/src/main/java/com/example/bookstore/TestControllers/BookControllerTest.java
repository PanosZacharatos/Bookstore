package com.example.bookstore.TestControllers;

import com.example.bookstore.Controllers.BookController;
import com.example.bookstore.Models.Book;
import com.example.bookstore.Services.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.when;

@WebMvcTest(value = BookController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;
    private ObjectMapper mapper = new ObjectMapper();

    // TESTS FOR GET ALL BOOKS

    // test with expected results
    @Test
    public void testGetAllBooks() throws Exception{
        //set up mock service
        when(bookService.findAllBooks()).thenReturn(Arrays.asList(
                new Book(1L, "Test Book 1", "Author 1", "9780451524935", new Date(), 19.45, 100),
                new Book(2L, "Test Book 2", "Author 2", "9780451524936", new Date(), 12.12, 99)

        ));

        //perform get request and verify the response is correct
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Test Book 1")))
                .andExpect(jsonPath("$[1].title", is("Test Book 2")));

    }

    //test with no results
    @Test
    public void testGetAllBooks_NoResults() throws Exception{
        //set up mock service
        when(bookService.findAllBooks()).thenReturn(Collections.emptyList());

        //perform get request and verify the response is correct
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(0)));
    }

    //test with service layer exception
    @Test
    public void testGetAllBooks_ServiceException() throws Exception{
        //set up mock service
        when(bookService.findAllBooks()).thenThrow(new RuntimeException("Failed to retrieve books"));

        //perform get request and verify the response is correct
        mockMvc.perform(get("/books"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Failed to retrieve books")));
    }

    //test for security
//    @Test
//    public void testGetAllBooks_Security() throws Exception{
        //TODO
//    }

    //test for data integrity
    @Test
    public void testGetAllBooks_DataIntegrity() throws Exception{
        //set up mock service
        when(bookService.findAllBooks()).thenReturn(Arrays.asList(
                new Book(1L, "Test Book 1", "Author 1", "9780451524935", new Date(), 19.45, 100),
                new Book(2L, "Test Book 2", "Author 2", "9780451524936", new Date(), 12.12, 99)

        ));

        //perform get request and verify the response is correct
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*].title", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*].author", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*].isbn", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*].publishedDate", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*].price", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*].quantity", everyItem(notNullValue())));
    }


    // TESTS FOR GET BOOK BY ID

    // test with expected results
    @Test
    public void testGetBookById() throws Exception{
        //set up mock service
        Long id = 1L;
        Book book = new Book(id,"Test Book 1", "Author 1", "9780451524935", new Date(), 19.45, 100);
        when(bookService.findBookById(id)).thenReturn(Optional.of(book));

        //perform get request and verify the response is correct
        mockMvc.perform(get("/books/{id}",id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Test Book 1")))
                .andExpect(jsonPath("$.author", is("Author 1")));
    }


    //test with no book found
    @Test
    public void testGetBookById_BookNotFound() throws Exception{
        //set up mock service
        Long id = 1L;
        when(bookService.findBookById(id)).thenReturn(Optional.empty());

        //perform get request and verify the response is correct
        mockMvc.perform(get("/books/{id}",id))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Book not found")));
    }

    //test with invalid id
    @Test
    public void testGetBookById_InvalidId() throws Exception{

        //perform get request and verify the response is correct
        mockMvc.perform(get("/books/{id}","error"))
                .andExpect(status().isBadRequest());
    }

    //test with service layer exception
    @Test
    public void testGetBookById_ServiceException() throws Exception {
        Long id = 1L;
        when(bookService.findBookById(id)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));

        mockMvc.perform(get("/books/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Book not found")));
    }

    //test for security
//    @Test
//    public void testGetBookById_Security() throws Exception{
    //TODO
//    }

    // TESTS FOR CREATE A NEW BOOK

    // test with expected results
//    @Test
//    public void createBook() throws Exception{
//        //set up mock service
//        Book book = new Book(null,"Test Book 1", "Author 1", "9780451524935", new Date(), 19.45, 100);
//        Book savedBook = new Book(1L,"Test Book 1", "Author 1", "9780451524935", new Date(), 19.45, 100);
//        when(bookService.saveBook(any(Book.class))).thenReturn(savedBook);
//
//        //perform get request and verify the response is correct
//        mockMvc.perform(post("/books"))
//
//
//    }

//    @Test
//    public void testCreateBook_Successful() throws Exception {
//        Book book = new Book(null, "New Book", "Author Name", "1234567890123", new Date(), 29.99, 50);
//        Book savedBook = new Book(1L, "New Book", "Author Name", "1234567890123", new Date(), 29.99, 50);
//        when(bookService.saveBook(any(Book.class))).thenReturn(savedBook);
//
//        mockMvc.perform(post("/books")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(book)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(1)))
//                .andExpect(jsonPath("$.title", is("New Book")));
//    }


    @Test
    public void testCreateBook_Successful() throws Exception {
        Book book = new Book(null, "New Book", "Author Name", "1234567890123", new Date(), 29.99, 50);
        Book savedBook = new Book(1L, "New Book", "Author Name", "1234567890123", new Date(), 29.99, 50);
        when(bookService.saveBook(any(Book.class))).thenReturn(savedBook);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1))) // Assuming 'id' is an integer in JSON
                .andExpect(jsonPath("$.title", is("New Book"))); // Correct use of 'is' matcher for string comparison
    }



}
