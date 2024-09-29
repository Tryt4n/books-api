package com.example.books_api.services;

import com.example.books_api.dtos.BookUpdateDTO;
import com.example.books_api.models.Book;
import com.example.books_api.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBookById() {
        Book book = new Book(1L, "Title", "Author", 2024, 4.5, 10, "1234567890", "Fiction", 300, "Publisher", "English");
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book foundBook = bookService.getBookById(1L);

        assertEquals(book.getTitle(), foundBook.getTitle());
        verify(bookRepository).findById(1L);
    }

    @Test
    void testGetBookById_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            bookService.getBookById(1L);
        });

        assertEquals("Book not found", exception.getMessage());
    }

    @Test
    void testGetAllBooks() {
        Book book1 = new Book(1L, "Title 1", "Author 1", 2024, 4.5, 10, "1234567890", "Fiction", 300, "Publisher", "English");
        Book book2 = new Book(2L, "Title 2", "Author 2", 2023, 4.0, 5, "0987654321", "Non-Fiction", 250, "Publisher", "Polish");
        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

        List<Book> books = bookService.getAllBooks();

        assertEquals(2, books.size());
        verify(bookRepository).findAll();
    }

    @Test
    void testCreateBook() {
        Book book = new Book(null, "New Book", "New Author", 2024, 5.0, 0, "0987654321", "Fiction", 250, "New Publisher", "English");
        when(bookRepository.save(book)).thenReturn(book);

        Book createdBook = bookService.createBook(book);

        assertEquals(book.getTitle(), createdBook.getTitle());
        verify(bookRepository).save(book);
    }

    @Test
    void testUpdateBookFields() {
        Book book = new Book(1L, "Old Title", "Old Author", 2024, 4.5, 10, "1234567890", "Fiction", 300, "Publisher", "English");
        BookUpdateDTO updateDTO = new BookUpdateDTO();
        updateDTO.setTitle("New Title");
        updateDTO.setAuthor("New Author");

        bookService.updateBookFields(book, updateDTO);

        assertEquals("New Title", book.getTitle());
        assertEquals("New Author", book.getAuthor());
    }

    @Test
    void testUpdateBook() {
        Book book = new Book(1L, "Old Title", "Old Author", 2024, 4.5, 10, "1234567890", "Fiction", 300, "Publisher", "English");
        when(bookRepository.save(book)).thenReturn(book);

        book.setTitle("Updated Title");
        Book updatedBook = bookService.updateBook(book);

        assertEquals("Updated Title", updatedBook.getTitle());
        verify(bookRepository).save(book);
    }

    @Test
    void testDeleteBook_Success() {
        when(bookRepository.existsById(1L)).thenReturn(true);

        bookService.deleteBook(1L);

        verify(bookRepository).deleteById(1L);
    }

    @Test
    void testDeleteBook_NotFound() {
        when(bookRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            bookService.deleteBook(1L);
        });

        assertEquals("Book not found", exception.getMessage());
    }

    @Test
    void testRateBook() {
        Book book = new Book(1L, "Title", "Author", 2024, 4.0, 1, "1234567890", "Fiction", 300, "Publisher", "English");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Book updatedBook = bookService.rateBook(1L, 5);

        assertNotNull(updatedBook);
        assertEquals(2, updatedBook.getNumberOfRatings());
        assertEquals(4.50, updatedBook.getRating(), 0.01);
        verify(bookRepository).save(book);
    }
}
