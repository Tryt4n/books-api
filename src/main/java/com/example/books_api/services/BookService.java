package com.example.books_api.services;

import com.example.books_api.dtos.BookUpdateDTO;
import com.example.books_api.enums.BookSortField;
import com.example.books_api.enums.SortOrder;
import com.example.books_api.enums.Direction;
import com.example.books_api.specifications.BookSpecification;
import com.example.books_api.models.Book;
import com.example.books_api.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    // Retrieve all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Retrieve book by ID
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
    }

    // Method to handle sorting of the books
    private Specification<Book> applySorting(Specification<Book> spec, BookSortField sortBy, SortOrder sortOrder) {
        // Determine the sort field
        String sortField = sortBy == BookSortField.NUMBEROFRATINGS
                ? "numberOfRatings" // Special case for "numberOfRatings"
                : (sortBy != null ? sortBy.name().toLowerCase() : "title"); // Default sort field is "title"

        // Default sort order is ascending if not specified
        SortOrder effectiveSortOrder = (sortOrder != null) ? sortOrder : SortOrder.ASC;

        // Apply sorting to the specification
        return Specification.where(spec).and((root, query, cb) -> {
            Objects.requireNonNull(query).orderBy(effectiveSortOrder == SortOrder.ASC
                    ? cb.asc(root.get(sortField))
                    : cb.desc(root.get(sortField)));
            return cb.conjunction();
        });
    }

    // Search books based on provided filters, ranges, and sorting criteria
    public List<Book> searchBooks(
            String title,
            String author,
            Integer year,
            Integer yearLimit,
            Direction yearDirection,
            Integer startYear,
            Integer endYear,
            Double rating,
            Double ratingLimit,
            Direction ratingDirection,
            Double minRating,
            Double maxRating,
            Integer numberOfRatings,
            Integer numberOfRatingsLimit,
            Direction numberOfRatingsDirection,
            Integer minNumberOfRatings,
            Integer maxNumberOfRatings,
            String isbn,
            String genre,
            Integer pages,
            Integer pagesLimit,
            Integer minPages,
            Integer maxPages,
            Direction pagesDirection,
            String publisher,
            String language,
            BookSortField sortBy,
            SortOrder sortOrder
    ) {
        // Build base search specification with filters
        Map<String, Object> filters = new HashMap<>();
        filters.put("title", title);
        filters.put("author", author);
        filters.put("year", year);
        filters.put("rating", rating);
        filters.put("numberOfRatings", numberOfRatings);
        filters.put("isbn", isbn);
        filters.put("genre", genre);
        filters.put("pages", pages);
        filters.put("publisher", publisher);
        filters.put("language", language);

        // Apply filters to the specification
        Specification<Book> spec = BookSpecification.applyFilters(Specification.where(null), filters);

        // Apply limits and ranges
        spec = BookSpecification.applyLimit(spec, "year", yearLimit, yearDirection, Integer.class);
        spec = BookSpecification.applyLimit(spec, "rating", ratingLimit, ratingDirection, Double.class);
        spec = BookSpecification.applyLimit(spec, "numberOfRatings", numberOfRatingsLimit, numberOfRatingsDirection, Integer.class);
        spec = BookSpecification.applyLimit(spec, "pages", pagesLimit, pagesDirection, Integer.class);

        if (startYear != null || endYear != null) {
            spec = spec.and(BookSpecification.hasYearBetween(startYear, endYear));
        }

        if (minRating != null || maxRating != null) {
            spec = spec.and(BookSpecification.hasRatingBetween(minRating, maxRating));
        }

        if (minNumberOfRatings != null || maxNumberOfRatings != null) {
            spec = spec.and(BookSpecification.hasNumberOfRatingsBetween(minNumberOfRatings, maxNumberOfRatings));
        }

        if (minPages != null || maxPages != null) {
            spec = spec.and(BookSpecification.hasPagesBetween(minPages, maxPages));
        }

        // Apply sorting criteria
        spec = applySorting(spec, sortBy, sortOrder);

        // Return filtered and sorted results
        return bookRepository.findAll(spec);
    }

    // Method to rate a book and update its average rating
    public Book rateBook(Long id, Integer newRating) {
        Book book = getBookById(id);

        // Update the number of ratings
        int updatedNumberOfRatings = book.getNumberOfRatings() + 1;
        double updatedRating = ((book.getRating() * book.getNumberOfRatings()) + newRating) / updatedNumberOfRatings;

        // Set rounded rating and updated number of ratings
        book.setNumberOfRatings(updatedNumberOfRatings);
        book.setRating(BigDecimal.valueOf(updatedRating).setScale(2, RoundingMode.HALF_UP).doubleValue());

        // Save and return the updated book
        return bookRepository.save(book);
    }

    // Method to create a new book
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    // Method to update the fields of an existing book
    public void updateBookFields(Book existingBook, BookUpdateDTO bookUpdateDTO) {
        Optional.ofNullable(bookUpdateDTO.getTitle()).ifPresent(existingBook::setTitle);
        Optional.ofNullable(bookUpdateDTO.getAuthor()).ifPresent(existingBook::setAuthor);
        Optional.ofNullable(bookUpdateDTO.getYear()).ifPresent(existingBook::setYear);
        Optional.ofNullable(bookUpdateDTO.getRating()).ifPresent(existingBook::setRating);
        Optional.ofNullable(bookUpdateDTO.getNumberOfRatings()).ifPresent(existingBook::setNumberOfRatings);
        Optional.ofNullable(bookUpdateDTO.getIsbn()).ifPresent(existingBook::setIsbn);
        Optional.ofNullable(bookUpdateDTO.getGenre()).ifPresent(existingBook::setGenre);
        Optional.ofNullable(bookUpdateDTO.getPages()).ifPresent(existingBook::setPages);
        Optional.ofNullable(bookUpdateDTO.getPublisher()).ifPresent(existingBook::setPublisher);
        Optional.ofNullable(bookUpdateDTO.getLanguage()).ifPresent(existingBook::setLanguage);
    }

    // Method to update an existing book
    public Book updateBook(Book book) {
        return bookRepository.save(book);
    }

    // Method to delete a book
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found");
        }
        bookRepository.deleteById(id);
    }
}