package com.example.books_api.exceptions;

import com.example.books_api.enums.BookSortField;
import com.example.books_api.enums.Direction;
import com.example.books_api.enums.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Logger for logging exceptions
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles all general exceptions.
     * Logs the error message and returns a response with HTTP status 500 (Internal Server Error).
     *
     * @param e the caught exception
     * @return a ResponseEntity with the error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        logger.error("An error occurred: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
    }

    /**
     * Handles MethodArgumentTypeMismatchException, which occurs when the type of request parameter
     * doesn't match the expected type.
     * Logs the error message and returns a response with HTTP status 400 (Bad Request).
     *
     * @param e the caught exception
     * @return a ResponseEntity with an informative message based on the type of the parameter
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        logger.error("Type mismatch: {}", e.getMessage(), e);
        String paramName = e.getName();

        // Check if the mismatched parameter is "rating"
        if ("rating".equals(paramName)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid value for rating. Rating must be an integer between 1 and 5.");
        }

        // For other enum types like BookSortField, Direction, or SortOrder, provide specific messages
        String enumType = Objects.requireNonNull(e.getRequiredType()).getSimpleName();
        String availableValues = "";

        // Check the type of the parameter and provide the available options
        if (enumType.equals("BookSortField")) {
            availableValues = "Available options: " + String.join(", ", BookSortField.names());
        } else if (enumType.equals("Direction")) {
            availableValues = "Available options: " + String.join(", ", Direction.names());
        } else if (enumType.equals("SortOrder")) {
            availableValues = "Available options: " + String.join(", ", SortOrder.names());
        }

        // Return a response with the parameter name, invalid value, and available options (for enums)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                String.format("Invalid value for parameter '%s': %s. %s", paramName, e.getValue(), availableValues)
        );
    }
}