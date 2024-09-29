package com.example.books_api.specifications;

import com.example.books_api.enums.Direction;
import com.example.books_api.models.Book;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;
import java.util.Optional;

public class BookSpecification {

    // Helper method for case-insensitive "like" queries
    public static Specification<Book> likeIgnoreCase(String attribute, String value) {
        return (book, query, cb) -> value == null ? null : cb.like(cb.lower(book.get(attribute)), "%" + value.toLowerCase() + "%");
    }

    // Helper method for equality checks
    public static Specification<Book> equal(String attribute, Object value) {
        return value == null ? null : (book, query, cb) -> cb.equal(book.get(attribute), value);
    }

    // Helper method for greater than or equal to checks
    public static <T extends Comparable<T>> Specification<Book> greaterThanOrEqualTo(
            String attribute, T value, Class<T> clazz
    ) {
        return value == null ? null : (book, query, cb) -> cb.greaterThanOrEqualTo(book.get(attribute).as(clazz), value);
    }

    // Helper method for less than or equal to checks
    public static <T extends Comparable<T>> Specification<Book> lessThanOrEqualTo(
            String attribute, T value, Class<T> clazz
    ) {
        return value == null ? null : (book, query, cb) -> cb.lessThanOrEqualTo(book.get(attribute).as(clazz), value);
    }

    // Helper method for between checks
    private static <T extends Comparable<? super T>> Specification<Book> betweenHelper(
            String attribute, T minValue, T maxValue, Class<T> clazz) {
        return (book, query, cb) -> {
            // Check if the min value is less than or equal to the max value, otherwise throw an exception
            if (minValue != null && maxValue != null) {
                if (minValue.compareTo(maxValue) > 0) {
                    throw new IllegalArgumentException(String.format("Min value must be less than or equal to max value for %s.", attribute));
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

    // Method to apply filters from map
    public static Specification<Book> applyFilters(Specification<Book> spec, Map<String, Object> filters) {
        for (Map.Entry<String, Object> filter : filters.entrySet()) {
            if (filter.getValue() != null) {
                // If the filter value is a string, use a case-insensitive "like" query
                if (filter.getValue() instanceof String) {
                    spec = spec.and(likeIgnoreCase(filter.getKey(), (String) filter.getValue()));
                } else {
                    // Otherwise, use an equality check
                    spec = spec.and(equal(filter.getKey(), filter.getValue()));
                }
            }
        }
        return spec;
    }

    // Method to apply limit based on direction
    public static <T extends Comparable<T>> Specification<Book> applyLimit(
            Specification<Book> spec, String field, T limit, Direction direction, Class<T> clazz
    ) {
        if (limit != null) {
            direction = Optional.ofNullable(direction).orElse(Direction.UP);
            if (direction == Direction.UP) {
                return spec.and(greaterThanOrEqualTo(field, limit, clazz));
            } else {
                return spec.and(lessThanOrEqualTo(field, limit, clazz));
            }
        }
        return spec;
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