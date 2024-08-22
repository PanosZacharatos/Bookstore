package com.example.bookstore.Repositories;

import com.example.bookstore.Models.Book;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookRepository extends JpaRepository<Book, Long>{
}
