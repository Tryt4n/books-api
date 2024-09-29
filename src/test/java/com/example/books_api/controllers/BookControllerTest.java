package com.example.books_api.controllers;

import com.example.books_api.dtos.BookUpdateDTO;
import com.example.books_api.models.Book;
import com.example.books_api.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookControllerTest {

    @InjectMocks
    private BookController bookController;

    @Mock
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBooks() {
        Book book1 = new Book(1L, "Title 1", "Author 1", 2024, 4.5, 10, "1234567890", "Fiction", 300, "Publisher", "English");
        Book book2 = new Book(2L, "Title 2", "Author 2", 2023, 4.0, 5, "0987654321", "Non-Fiction", 250, "Publisher", "Polish");
        when(bookService.getAllBooks()).thenReturn(List.of(book1, book2));

        List<Book> books = bookController.getAllBooks();

        assertEquals(2, books.size());
        verify(bookService).getAllBooks();
    }

    @Test
    void testGetBookById() {
        Book book = new Book(1L, "Title", "Author", 2024, 4.5, 10, "1234567890", "Fiction", 300, "Publisher", "English");
        when(bookService.getBookById(1L)).thenReturn(book);

        Book foundBook = bookController.getBookById(1L);

        assertEquals(book.getTitle(), foundBook.getTitle());
        verify(bookService).getBookById(1L);
    }

    @Test
    void testCreateBook() {
        Book book = new Book(null, "New Book", "New Author", 2024, 5.0, 0, "0987654321", "Fiction", 250, "New Publisher", "English");
        when(bookService.createBook(book)).thenReturn(book);

        Book createdBook = bookController.createBook(book);

        assertEquals(book.getTitle(), createdBook.getTitle());
        verify(bookService).createBook(book);
    }

    @Test
    void testCreateBook_InvalidTitle() {
        Book book = new Book(null, null, "New Author", 2024, 5.0, 0, "0987654321", "Fiction", 250, "New Publisher", "English");

        doThrow(new IllegalArgumentException("Title is required."))
                .when(bookService).createBook(book);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bookController.createBook(book);
        });

        assertEquals("Title is required.", exception.getMessage());

        verify(bookService).createBook(book);
    }

    @Test
    void testUpdateBook_Success() {
        Book existingBook = new Book(1L, "Old Title", "Old Author", 2024, 4.5, 10, "1234567890", "Fiction", 300, "Publisher", "English");
        BookUpdateDTO updateDTO = new BookUpdateDTO();
        updateDTO.setTitle("New Title");
        updateDTO.setAuthor("New Author");

        when(bookService.getBookById(1L)).thenReturn(existingBook);
        when(bookService.updateBook(existingBook)).thenReturn(existingBook);

        ResponseEntity<Book> response = bookController.updateBook(1L, updateDTO);

        assertEquals(200, response.getStatusCode().value());
        verify(bookService).updateBookFields(existingBook, updateDTO);
        verify(bookService).updateBook(existingBook);
    }

    @Test
    void testUpdateBook_InvalidTitle() {
        Book existingBook = new Book(1L, "Old Title", "Old Author", 2024, 4.5, 10, "1234567890", "Fiction", 300, "Publisher", "English");
        BookUpdateDTO updateDTO = new BookUpdateDTO();
        updateDTO.setTitle(null);

        when(bookService.getBookById(1L)).thenReturn(existingBook);
        when(bookService.updateBook(existingBook)).thenReturn(existingBook);

        ResponseEntity<Book> response = bookController.updateBook(1L, updateDTO);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(existingBook.getTitle(), Objects.requireNonNull(response.getBody()).getTitle());
        verify(bookService).updateBookFields(existingBook, updateDTO);
        verify(bookService).updateBook(existingBook);
    }

    @Test
    void testUpdateBook_NotFound() {
        BookUpdateDTO updateDTO = new BookUpdateDTO();
        updateDTO.setTitle("New Title");

        when(bookService.getBookById(1L)).thenReturn(null);

        ResponseEntity<Book> response = bookController.updateBook(1L, updateDTO);

        assertEquals(404, response.getStatusCode().value());
        verify(bookService).getBookById(1L);
    }

    @Test
    void testDeleteBook() {
        doNothing().when(bookService).deleteBook(1L);

        Map<String, String> response = bookController.deleteBook(1L);

        assertEquals("Book deleted successfully.", response.get("message"));
        verify(bookService).deleteBook(1L);
    }

    @Test
    void testDeleteBook_NotFound() {
        doThrow(new IllegalArgumentException("Book not found"))
                .when(bookService).deleteBook(999L);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookController.deleteBook(999L);
        });

        assertEquals("Book not found", exception.getMessage());

        verify(bookService).deleteBook(999L);
    }

    @Test
    void testRateBook() {
        Book book = new Book(1L, "Title", "Author", 2024, 4.0, 1, "1234567890", "Fiction", 300, "Publisher", "English");
        when(bookService.rateBook(1L, 5)).thenReturn(book);

        Book ratedBook = bookController.rateBook(1L, 5);

        assertEquals(book.getTitle(), ratedBook.getTitle());
        verify(bookService).rateBook(1L, 5);
    }

    @Test
    void testRateBook_InvalidId() {
        Long invalidId = 999L;
        Integer rating = 5;

        doThrow(new IllegalArgumentException("Book not found"))
                .when(bookService).rateBook(invalidId, rating);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookController.rateBook(invalidId, rating);
        });

        assertEquals("Book not found", exception.getMessage());

        verify(bookService).rateBook(invalidId, rating);
    }

    @Test
    void testRateBook_InvalidRating() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookController.rateBook(1L, 6);
        });

        assertEquals("Rating must be an integer between 1 and 5.", exception.getMessage());
    }

    @Test
    void testSearchBooks_WithTitleAndAuthor() {
        Book book1 = new Book(1L, "Title", "Author", 2024, 4.0, 1, "1234567890", "Fiction", 300, "Publisher", "English");
        Book book2 = new Book(2L, "Title2", "Author2", 2023, 4.2, 5, "1234567892", "Horror", 322, "Publisher2", "French");

        when(bookService.searchBooks("Title", "Author", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null))
                .thenReturn(List.of(book1, book2));

        List<Book> foundBooks = bookController.searchBooks("Title", "Author", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,null);

        assertEquals(2, foundBooks.size());
        verify(bookService).searchBooks("Title", "Author", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,null);
    }
}