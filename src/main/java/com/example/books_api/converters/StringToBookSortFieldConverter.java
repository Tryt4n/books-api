package com.example.books_api.converters;

import com.example.books_api.enums.BookSortField;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToBookSortFieldConverter implements Converter<String, BookSortField> {
    @Override
    public BookSortField convert(String source) {
        return BookSortField.valueOf(source.toUpperCase());
    }
}
