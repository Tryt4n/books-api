package com.example.books_api.enums;

import java.util.stream.Stream;

public enum Direction {
    UP,
    DOWN;

    public static String[] names() {
        return Stream.of(values()).map(Enum::name).toArray(String[]::new);
    }
}
