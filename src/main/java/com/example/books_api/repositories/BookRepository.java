package com.example.books_api.repositories;

import com.example.books_api.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository // Marks this interface as a Spring Data Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    // JpaRepository provides CRUD operations for Book entities
    // JpaSpecificationExecutor allows for complex queries using Specifications, supporting dynamic filtering and sorting
}
