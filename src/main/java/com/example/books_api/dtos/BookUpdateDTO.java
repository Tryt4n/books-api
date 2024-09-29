package com.example.books_api.dtos;

import lombok.Getter;
import lombok.Setter;

// This class serves as a Data Transfer Object (DTO) for updating book information
@Setter  // Lombok annotation to automatically generate setter methods for all fields
@Getter  // Lombok annotation to automatically generate getter methods for all fields
public class BookUpdateDTO {
    private String title;
    private String author;
    private Integer year;
    private Double rating;
    private Integer numberOfRatings;
    private String isbn;
    private String genre;
    private Integer pages;
    private String publisher;
    private String language;
}
