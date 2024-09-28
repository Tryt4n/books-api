package com.example.books_api.specifications;

import com.example.books_api.models.Book;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

    // Helper method for case-insensitive "like" queries
    private static Specification<Book> likeIgnoreCase(String attribute, String value) {
        return (book, query, cb) -> value == null ? null : cb.like(cb.lower(book.get(attribute)), "%" + value.toLowerCase() + "%");
    }

    // Helper method for equality checks
    private static Specification<Book> equal(String attribute, Object value) {
        return value == null ? null : (book, query, cb) -> cb.equal(book.get(attribute), value);
    }

    // Helper method for greater than or equal to checks
    public static <T extends Comparable<T>> Specification<Book> greaterThanOrEqualTo(String attribute, T value, Class<T> clazz) {
        return value == null ? null : (book, query, cb) -> cb.greaterThanOrEqualTo(book.get(attribute).as(clazz), value);
    }

    // Helper method for less than or equal to checks
    public static <T extends Comparable<T>> Specification<Book> lessThanOrEqualTo(String attribute, T value, Class<T> clazz) {
        return value == null ? null : (book, query, cb) -> cb.lessThanOrEqualTo(book.get(attribute).as(clazz), value);
    }

    // Helper method for between checks
    private static <T extends Comparable<? super T>> Specification<Book> betweenHelper(
            String attribute, T minValue, T maxValue, Class<T> clazz) {
        return (book, query, cb) -> {
            // Check if the min value is less than or equal to the max value, otherwise throw an exception
            if (minValue != null && maxValue != null) {
                if (minValue.compareTo(maxValue) > 0) {
                    throw new IllegalArgumentException("Min value must be less than or equal to max value for " + attribute + ".");
                }
                // If both parameters are provided, search for books between the range (inclusive)
                return cb.between(book.get(attribute).as(clazz), minValue, maxValue);
            } else if (minValue != null) {
                // If only min value is provided, search for books from min value upwards
                return cb.greaterThanOrEqualTo(book.get(attribute).as(clazz), minValue);
            } else if (maxValue != null) {
                // If only max value is provided, search for books up to max value downwards
                return cb.lessThanOrEqualTo(book.get(attribute).as(clazz), maxValue);
            }
            return null;
        };
    }

    public static Specification<Book> hasTitle(String title) {
        return likeIgnoreCase("title", title);
    }

    public static Specification<Book> hasAuthor(String author) {
        return likeIgnoreCase("author", author);
    }

    public static Specification<Book> hasYear(Integer year) {
        return equal("year", year);
    }

    public static Specification<Book> hasRating(Double rating) {
        return equal("rating", rating);
    }

    public static Specification<Book> hasNumberOfRatings(Integer numberOfRatings) {
        return equal("numberOfRatings", numberOfRatings);
    }

    public static Specification<Book> hasIsbn(String isbn) {
        return equal("isbn", isbn);
    }

    public static Specification<Book> hasGenre(String genre) {
        return likeIgnoreCase("genre", genre);
    }

    public static Specification<Book> hasPages(Integer pages) {
        return equal("pages", pages);
    }

    public static Specification<Book> hasPublisher(String publisher) {
        return likeIgnoreCase("publisher", publisher);
    }

    public static Specification<Book> hasLanguage(String language) {
        return likeIgnoreCase("language", language);
    }

    public static Specification<Book> hasYearGreaterThan(int year) {
        return greaterThanOrEqualTo("year", year, Integer.class);
    }

    public static Specification<Book> hasYearLessThan(int year) {
        return lessThanOrEqualTo("year", year, Integer.class);
    }

    public static Specification<Book> hasRatingGreaterThan(double rating) {
        return greaterThanOrEqualTo("rating", rating, Double.class);
    }

    public static Specification<Book> hasRatingLessThan(double rating) {
        return lessThanOrEqualTo("rating", rating, Double.class);
    }

    public static Specification<Book> hasNumberOfRatingsGreaterThan(int numberOfRatings) {
        return greaterThanOrEqualTo("numberOfRatings", numberOfRatings, Integer.class);
    }

    public static Specification<Book> hasNumberOfRatingsLessThan(int numberOfRatings) {
        return lessThanOrEqualTo("numberOfRatings", numberOfRatings, Integer.class);
    }

    public static Specification<Book> hasPagesGreaterThan(int pages) {
        return greaterThanOrEqualTo("pages", pages, Integer.class);
    }

    public static Specification<Book> hasPagesLessThan(int pages) {
        return lessThanOrEqualTo("pages", pages, Integer.class);
    }

    public static Specification<Book> hasYearBetween(Integer startYear, Integer endYear) {
        return betweenHelper("year", startYear, endYear, Integer.class);
    }

    public static Specification<Book> hasRatingBetween(Double minRating, Double maxRating) {
        return betweenHelper("rating", minRating, maxRating, Double.class);
    }

    public static Specification<Book> hasNumberOfRatingsBetween(Integer minNumberOfRatings, Integer maxNumberOfRatings) {
        return betweenHelper("numberOfRatings", minNumberOfRatings, maxNumberOfRatings, Integer.class);
    }

    public static Specification<Book> hasPagesBetween(Integer minPages, Integer maxPages) {
        return betweenHelper("pages", minPages, maxPages, Integer.class);
    }
}