package com.example.books_api.enums;

import java.util.stream.Stream;

public enum BookSortField {
    TITLE, AUTHOR, YEAR, RATING, NUMBEROFRATINGS, ISBN, GENRE, PAGES, PUBLISHER, LANGUAGE;

    public static String[] names() {
        return Stream.of(values()).map(Enum::name).toArray(String[]::new);
    }
}
