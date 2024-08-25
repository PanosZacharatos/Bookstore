package com.example.bookstore.Repositories;

import com.example.bookstore.Models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookRepository extends JpaRepository<Book, Long>{
    Page<Book> findByTitle(String title, Pageable pageable);
    Page<Book> findByAuthor(String author, Pageable pageable);
}
