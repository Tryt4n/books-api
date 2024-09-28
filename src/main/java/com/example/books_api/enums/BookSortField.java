package com.example.books_api.enums;

import java.util.stream.Stream;

public enum BookSortField {
    TITLE, AUTHOR, YEAR, RATING, NUMBER_OF_RATINGS, ISBN, GENRE, PAGES, PUBLISHER, LANGUAGE;

    public static String[] names() {
        return Stream.of(values()).map(Enum::name).toArray(String[]::new);
    }

    public static BookSortField fromString(String sortField) {
        return switch (sortField.toLowerCase()) {
            case "author" -> AUTHOR;
            case "year" -> YEAR;
            case "rating" -> RATING;
            case "number_of_ratings" -> NUMBER_OF_RATINGS;
            case "isbn" -> ISBN;
            case "genre" -> GENRE;
            case "pages" -> PAGES;
            case "publisher" -> PUBLISHER;
            case "language" -> LANGUAGE;
            default -> TITLE;
        };
    }
}
