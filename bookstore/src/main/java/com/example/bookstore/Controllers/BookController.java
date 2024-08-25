package com.example.bookstore.Controllers;

import com.example.bookstore.Models.Book;
import com.example.bookstore.Services.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestController
@RequestMapping("/books")
@Valid
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<?> getAllBooks(@PageableDefault(size = 10) Pageable pageable,@RequestParam(required = false)String title,@RequestParam(required = false)String author) {
        //return bookService.findAllBooks();
        try {
            Page<Book> books;
            if(title != null){
                books = bookService.findBookByTitle(title,pageable);
            }
            else if(author != null){
                books = bookService.findBookByAuthor(author,pageable);
            }
            else{
                books = bookService.findAllBooks(pageable);
            }
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            //return exception in case no response entity returned
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve books");
        }
    }

    // return Entity if book with given id exists or 404 not found
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id){
        try {
            //attempt to find the book
            return bookService.findBookById(id)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
        } catch (ResponseStatusException e) {
            //catch the ResponseStatusException if the book is not found
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    @PostMapping
    public ResponseEntity<?> createBook(@Valid @RequestBody Book book){
        //return bookService.saveBook(book);
        try {
            Book savedBook = bookService.saveBook(book);
            return ResponseEntity.ok(savedBook);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid book data provided");
        }
    }

    //return entity with updated fields or 404 not found
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id,@Valid @RequestBody Book book) {
        try {
            return bookService.findBookById(id)
                    .map(existingBook -> {
                        existingBook.setTitle(book.getTitle());
                        existingBook.setAuthor(book.getAuthor());
                        existingBook.setIsbn(book.getIsbn());
                        existingBook.setPublishedDate(book.getPublishedDate());
                        existingBook.setPrice(book.getPrice());
                        existingBook.setQuantity(book.getQuantity());
                        return ResponseEntity.ok(bookService.saveBook(existingBook));
                    })
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    //delete book by its id or 404 not found
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        try {
            return bookService.findBookById(id)
                    .map(book -> {
                        bookService.deleteBook(id);
                        return ResponseEntity.ok().build();
                    })
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
}
