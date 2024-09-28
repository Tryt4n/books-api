package com.example.books_api.converters;

import com.example.books_api.enums.Direction;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToDirectionConverter implements Converter<String, Direction> {
    @Override
    public Direction convert(String source) {
        return Direction.valueOf(source.toUpperCase());
    }
}
