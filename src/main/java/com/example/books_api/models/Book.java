package com.example.books_api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // Marks this class as a JPA entity (it will be mapped to a database table)
@Table(name = "book") // Specifies the table name for this entity in the database
@Data // Lombok annotation to auto-generate getters, setters, equals, hashCode, and toString methods
@NoArgsConstructor // Lombok annotation to auto-generate a no-arguments constructor
@AllArgsConstructor // Lombok annotation to auto-generate a constructor with all fields as arguments
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Specifies that the ID will be generated automatically by the database (auto-increment)
    private Long id;
    private String title;
    private String author;

    @Column(name = "publication_year")
    private int year;

    private double rating;

    @Column(name = "number_of_ratings")
    private int numberOfRatings;

    private String isbn;
    private String genre;
    private int pages;
    private String publisher;

    @Column(name = "publication_language")
    private String language;
}
