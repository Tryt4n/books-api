package com.example.books_api.services;

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
import java.util.List;
import java.util.Objects;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
    }

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
        Specification<Book> spec = Specification.where(null);

        if (title != null && !title.isEmpty()) {
            spec = spec.and(BookSpecification.hasTitle(title));
        }
        if (author != null && !author.isEmpty()) {
            spec = spec.and(BookSpecification.hasAuthor(author));
        }
        if (year != null) {
            spec = spec.and(BookSpecification.hasYear(year));
        }
        if (rating != null) {
            spec = spec.and(BookSpecification.hasRating(rating));
        }
        if (numberOfRatings != null) {
            spec = spec.and(BookSpecification.hasNumberOfRatings(numberOfRatings));
        }
        if (isbn != null && !isbn.isEmpty()) {
            spec = spec.and(BookSpecification.hasIsbn(isbn));
        }
        if (genre != null && !genre.isEmpty()) {
            spec = spec.and(BookSpecification.hasGenre(genre));
        }
        if (pages != null) {
            spec = spec.and(BookSpecification.hasPages(pages));
        }
        if (publisher != null && !publisher.isEmpty()) {
            spec = spec.and(BookSpecification.hasPublisher(publisher));
        }
        if (language != null && !language.isEmpty()) {
            spec = spec.and(BookSpecification.hasLanguage(language));
        }

        // Obsługa yearLimit i direction
        if (yearLimit != null) {
            yearDirection = yearDirection == null ? Direction.UP : yearDirection;

            spec = switch (yearDirection) {
                case UP -> spec.and(BookSpecification.hasYearGreaterThan(yearLimit));
                case DOWN -> spec.and(BookSpecification.hasYearLessThan(yearLimit));
            };
        }

        // Obsługa ratingLimit i ratingDirection
        if (ratingLimit != null) {
            ratingDirection = ratingDirection == null ? Direction.UP : ratingDirection;

            spec = switch (ratingDirection) {
                case UP -> spec.and(BookSpecification.hasRatingGreaterThan(ratingLimit));
                case DOWN -> spec.and(BookSpecification.hasRatingLessThan(ratingLimit));
            };
        }

        if (numberOfRatingsLimit != null) {
            numberOfRatingsDirection = numberOfRatingsDirection == null ? Direction.UP : numberOfRatingsDirection;

            spec = switch (numberOfRatingsDirection) {
                case UP -> spec.and(BookSpecification.hasNumberOfRatingsGreaterThan(numberOfRatingsLimit));
                case DOWN -> spec.and(BookSpecification.hasNumberOfRatingsLessThan(numberOfRatingsLimit));
            };
        }

        // Obsługa pagesLimit i pagesDirection
        if (pagesLimit != null) {
            pagesDirection = pagesDirection == null ? Direction.UP : pagesDirection;

            spec = switch (pagesDirection) {
                case UP -> spec.and(BookSpecification.hasPagesGreaterThan(pagesLimit));
                case DOWN -> spec.and(BookSpecification.hasPagesLessThan(pagesLimit));
            };
        }

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

        // Obsługa sortowania
        String sortField = (sortBy == null) ? "title" : sortBy.name().toLowerCase(); // Domyślne sortowanie po tytule

        SortOrder effectiveSortOrder = (sortOrder != null) ? sortOrder : SortOrder.ASC;

        spec = Specification.where(spec).and((root, query, cb) -> {
            Objects.requireNonNull(query).orderBy(effectiveSortOrder == SortOrder.ASC ?
                    cb.asc(root.get(sortField)) : cb.desc(root.get(sortField)));
            return cb.conjunction();
        });

        return bookRepository.findAll(spec);
    }

    public Book rateBook(Long id, Integer newRating) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));

        // Zaktualizowanie liczby ocen
        int currentNumberOfRatings = book.getNumber_of_ratings();
        int updatedNumberOfRatings = currentNumberOfRatings + 1;
        book.setNumber_of_ratings(updatedNumberOfRatings);

        // Obliczenie nowego średniego ratingu
        double currentRating = book.getRating();
        double updatedRating = ((currentRating * currentNumberOfRatings) + newRating) / updatedNumberOfRatings;

        // Zaokrąglenie do dwóch miejsc po przecinku
        BigDecimal roundedRating = new BigDecimal(updatedRating).setScale(2, RoundingMode.HALF_UP);

        book.setRating(roundedRating.doubleValue());

        // Zapisanie zaktualizowanej książki w repozytorium
        return bookRepository.save(book);
    }
}
