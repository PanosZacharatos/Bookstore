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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

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
    @Test
    public void testCreateBook() throws Exception{
        Book book = new Book(null,"Book 1","Author 1","1234567890123",new Date(),20.00,20);
        when(bookService.saveBook(org.mockito.Mockito.any(Book.class))).thenReturn(book);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Book 1")));
    }

    // test with validation failure
    @Test
    public void testCreateBook_ValidationFail() throws Exception{
        Book book = new Book(null,"","","1234567890123",new Date(),20.00,20);
        when(bookService.saveBook(org.mockito.Mockito.any(Book.class))).thenReturn(book);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(book)))
                .andExpect(status().isBadRequest());

    }

    // test with service exception
    @Test
    public void testCreateBook_ServiceException() throws Exception{
        Book book = new Book(null,"Book 1","Author 1","1234567890123",new Date(),20.00,20);
        when(bookService.saveBook(org.mockito.Mockito.any(Book.class))).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(book)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid book data provided")));

    }

    // test with null data
    @Test
    public void testCreateBook_NullData() throws Exception{
        Book book = new Book(null,"Book 1",null,"1234567890123",new Date(),20.00,20);
        //when(bookService.saveBook(any(Book.class))).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(book)))
                .andExpect(status().isBadRequest());

    }

    //test for security
//    @Test
//    public void testCreateBook_Security() throws Exception{
            //TODO
//    }


    // TESTS FOR UPDATE A BOOK

    // test with expected results
    @Test
    public void testUpdateBook() throws Exception{
        Long id = 1L;
        Book book = new Book(id,"title","author","1234567890123",new Date(),20.00,20);
        Book updatedBook = new Book(id,"Updated title","author","1234567890123",new Date(),20.00,20);
        when(bookService.findBookById(id)).thenReturn(Optional.of(book));
        when(bookService.saveBook(org.mockito.Mockito.any((Book.class)))).thenReturn(updatedBook);


        mockMvc.perform(put("/books/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated title")));
    }

    // test with validation failure
    @Test
    public void testUpdateBook_ValidationFailure() throws Exception{
        Long id = 1L;
        Book updatedBook = new Book(id,"","","1234567890123",new Date(),20.00,20);



        mockMvc.perform(put("/books/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedBook)))
                .andExpect(status().isBadRequest());

    }

    // test with service exception
    @Test
    public void testUpdateBook_ServiceException() throws Exception{
        Long id = 1L;
        Book updatedBook = new Book(id,"title","author","1234567890123",new Date(),20.00,20);
        when(bookService.findBookById(id)).thenReturn(Optional.empty()); //simulate book not found


        mockMvc.perform(put("/books/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedBook)))
                .andExpect(status().isNotFound())
                .andExpect(content().string((containsString("Book not found"))));

    }

    //test for security
//    @Test
//    public void testUpdateBook_Security() throws Exception{
    //TODO
//    }

    // TESTS FOR DELETE A BOOK

    // test with expected results
    @Test
    public void testDeleteBook() throws Exception{
        Long id = 1L;
        Book existingBook = new Book();
        existingBook.setId(id);
        when(bookService.findBookById(id)).thenReturn(Optional.of(existingBook));
        doNothing().when(bookService).deleteBook(id);

        mockMvc.perform(delete("/books/{id}", id))
                .andExpect(status().isOk());
    }

    // test with book not found
    @Test
    public void testDeleteBook_BookNotFound() throws Exception{
        Long id = 1L;
        when(bookService.findBookById(id)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/books/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string((containsString("Book not found"))));
    }

    //test for security
//    @Test
//    public void testDeleteBook_Security() throws Exception{
    //TODO
//    }


}




