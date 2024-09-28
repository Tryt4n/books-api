package com.example.books_api.enums;

import java.util.stream.Stream;

public enum SortOrder {
    ASC,
    DESC;

    public static String[] names() {
        return Stream.of(values()).map(Enum::name).toArray(String[]::new);
    }
}
