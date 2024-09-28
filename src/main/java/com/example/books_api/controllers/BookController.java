package com.example.books_api.controllers;

import com.example.books_api.enums.BookSortField;
import com.example.books_api.enums.Direction;
import com.example.books_api.enums.SortOrder;
import com.example.books_api.models.Book;
import com.example.books_api.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController  // Marks this class as a RESTful controller
@RequestMapping("/api/books")  // Sets the base URL for all endpoints in this controller
public class BookController {
    @Autowired // Automatically injects the BookService bean
    private BookService bookService;

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    // Endpoint for searching books with optional filters
    @GetMapping("/search")
    public List<Book> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer yearLimit,
            @RequestParam(required = false) Direction yearDirection,
            @RequestParam(required = false) Integer startYear,
            @RequestParam(required = false) Integer endYear,
            @RequestParam(required = false) Double rating,
            @RequestParam(required = false) Double ratingLimit,
            @RequestParam(required = false) Direction ratingDirection,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double maxRating,
            @RequestParam(required = false) Integer numberOfRatings,
            @RequestParam(required = false) Integer numberOfRatingsLimit,
            @RequestParam(required = false) Direction numberOfRatingsDirection,
            @RequestParam(required = false) Integer minNumberOfRatings,
            @RequestParam(required = false) Integer maxNumberOfRatings,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Integer pages,
            @RequestParam(required = false) Integer pagesLimit,
            @RequestParam(required = false) Integer minPages,
            @RequestParam(required = false) Integer maxPages,
            @RequestParam(required = false) Direction pagesDirection,
            @RequestParam(required = false) String publisher,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) BookSortField sortBy,
            @RequestParam(required = false) SortOrder sortOrder
            ) {
        return bookService.searchBooks(
                title,
                author,
                year,
                yearLimit,
                yearDirection,
                startYear,
                endYear,
                rating,
                ratingLimit,
                ratingDirection,
                minRating,
                maxRating,
                numberOfRatings,
                numberOfRatingsLimit,
                numberOfRatingsDirection,
                minNumberOfRatings,
                maxNumberOfRatings,
                isbn,
                genre,
                pages,
                pagesLimit,
                minPages,
                maxPages,
                pagesDirection,
                publisher,
                language,
                sortBy,
                sortOrder
        );
    }

    @PostMapping("/{id}/rate")
    public Book rateBook(@PathVariable Long id, @RequestParam Integer rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be an integer between 1 and 5.");
        }

        return bookService.rateBook(id, rating);
    }
}
