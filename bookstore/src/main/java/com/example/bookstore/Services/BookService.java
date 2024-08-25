package com.example.bookstore.Services;

import com.example.bookstore.Models.Book;
import com.example.bookstore.Repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public Page<Book> findAllBooks(Pageable pageable){
        return bookRepository.findAll(pageable);
    }

    public Optional<Book> findBookById(Long id){
        return bookRepository.findById(id);
    }

    public Book saveBook(Book book){
        return bookRepository.save(book);
    }

    public void deleteBook(Long id){
        bookRepository.deleteById(id);
    }

}
