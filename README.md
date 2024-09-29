# Book RESTful API

## Table of Contents
- [Overview](#overview)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [API Endpoints](#endpoints)
  - [Get All Books](#get-all-books)
  - [Get Book By ID](#get-book-by-id)
  - [Create Book](#create-book)
  - [Update Book](#update-book)
  - [Delete Book](#delete-book)
  - [Rate Book](#rate-book)
  - [Search Books](#search-books)
    - [Filter Books by Title](#filter-books-by-title)
    - [Filter Books by Rating](#filter-books-by-rating)
    - [Sort Books below/above a specified year](#sort-books-belowabove-a-specified-year)
    - [Filter Books by rating within a range](#filter-books-by-rating-within-a-range)
    - [Combine Filters](#combine-filters)
- [Error Handling](#error-handling)

## Overview
The Book RESTful API provides a simple interface to manage a books database, including functionality to search, filter, sort, and rate books. The API supports dynamic filtering based on various attributes such as title, author, publication year, rating, genre, language, and more.

## Project Structure
    src/
    ├── main/
    │   ├── java/
    │   │   └── com.example.books_api/
    │   │       ├── controllers/
    │   │       │   └── BookController.java    
    │   │       ├── converters/
    │   │       │   └── StringToBookSortFieldConverter.java
    │   │       │   └── StringToDirectionConverter.java
    │   │       │   └── StringToSortOrderConverter.java
    │   │       ├── dtos/
    │   │       │   └── BookUpdateDTO.java
    │   │       ├── enums/
    │   │       │   ├── BookSortField.java
    │   │       │   ├── Direction.java
    │   │       │   └── SortOrder.java
    │   │       ├── exceptions/
    │   │       │   ├── GlobalExceptionHandler.java
    │   │       ├── models/
    │   │       │   └── Book.java
    │   │       ├── repositories/
    │   │       │   └── BookRepository.java
    │   │       ├── services/
    │   │       │   └── BookService.java
    │   │       │── specifications/
    │   │       │    └── BookSpecification.java
    │   │       └── BooksApiApplication.java
    │   ├── resources/
    │   │   ├── application.properties
    │   │   ├── schema.sql
    │   │   └── data.sql
    └── test/
    └── java/
    └── com.example.books_api/
    └── BookApiTest.java

## Getting Started

### Prerequisites
To run this application locally, you need the following:
- Java 23
- Spring Boot 3.x
- Spring Data JPA (Java Persistence API)
- H2 or another relational database (configured via application.properties)
- Maven (for dependencies)

### Installation
1. Clone the repository:
    ```bash
    git clone https://github.com/your-username/book-api.git
    cd book-api
    ```

2. Setup the database (update the `application.properties` with your database credentials):
    ```properties
   # In case of H2 database (otherwise update with your database credentials):
    spring.datasource.url=jdbc:h2:mem:database_name
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    ```

3. Run the application:
    ```bash
    ./mvnw spring-boot:run
    ```

4. The API will be available at `http://localhost:8080` (or another port if configured differently).

5. You can access the H2 console at `http://localhost:8080/h2-console` to view the in-memory database.

## Endpoints

### Get All Books
- **Endpoint**: `/books`
- **Method**: `GET`
- **Description**: Fetches a list of all books.
- **Response Example**:
    ```json
    [
      {
        "id": 1,
        "title": "To Kill a Mockingbird",
        "author": "Harper Lee",
        "year": 1960,
        "rating": 4.8,
        "numberOfRatings": 50,
        "isbn": "978-0061120084",
        "genre": "Fiction",
        "pages": 324,
        "publisher": "J.B. Lippincott & Co.",
        "language": "English"
      },
      {
        "id": 2,
        "title": "1984",
        "author": "George Orwell",
        "year": 1949,
        "rating": 4.7,
        "numberOfRatings": 1005,
        "isbn": "978-0451524935",
        "genre": "Dystopian",
        "pages": 328,
        "publisher": "Secker & Warburg",
        "language": "English"
      },
      ... // more books
    ]
    ```

### Get Book By ID
- **Endpoint**: `/books/{id}`
- **Method**: `GET`
- **Description**: Fetches a single book by its ID.
- **Response Example**:
    ```json
    {
        "id": 1,
        "title": "To Kill a Mockingbird",
        "author": "Harper Lee",
        "year": 1960,
        "rating": 4.8,
        "numberOfRatings": 50,
        "isbn": "978-0061120084",
        "genre": "Fiction",
        "pages": 324,
        "publisher": "J.B. Lippincott & Co.",
        "language": "English"
    }
    ```

### Create Book
- **Endpoint**: `/books/create`
- **Method**: `POST`
- **Description**: Creates a new book.
- **Request Body**:
    ```json
    {
        "title": "New Book Title",
        "author": "New Author",
        "year": 2021,
        "rating": 4.7,
        "numberOfRatings": 300,
        "isbn": "0987654321",
        "genre": "Non-Fiction",
        "pages": 200,
        "publisher": "New Publisher",
        "language": "Spanish"
    }
    ```
- **Response Example**:
    ```json
    {
        "id": 2,
        "title": "New Book Title",
        "author": "New Author",
        "year": 2021,
        "rating": 4.7,
        "numberOfRatings": 300,
        "isbn": "0987654321",
        "genre": "Non-Fiction",
        "pages": 200,
        "publisher": "New Publisher",
        "language": "Spanish"
    }
    ```

### Update Book
- **Endpoint**: `/books/update/{id}`
- **Method**: `PUT`
- **Description**: Updates an existing book. Can update any existing field except the id. Fields not provided will remain unchanged.
- **Request Body**:
    ```json
    {
        "publisher": "Updated Publisher",
    }
    ```
- **Response Example**:
    ```json
    {
        "id": 2,
        "title": "Updated Book Title",
        "author": "Updated Author",
        "year": 2022,
        "rating": 4.8,
        "numberOfRatings": 400,
        "isbn": "1122334455",
        "genre": "Science Fiction",
        "pages": 350,
        "publisher": "Updated Publisher",
        "language": "German"
    }
    ```

### Delete Book
- **Endpoint**: `/books/delete/{id}`
- **Method**: `DELETE`
- **Description**: Deletes a book by its ID.
- **Response Example**:
    ```json
    {
        "message": "Book deleted successfully."
    }
    ```
  
### Rate Book
- **Endpoint**: `/books/rate/{id}`
- **Method**: `POST`
- **Description**: Rates a book by its ID.
- **Request Body**:
    - `rating`: an integer between 1 and 5.
- **Response Example**:
    ```json
    {
        "id": 2,
        "title": "Updated Book Title",
        "author": "Updated Author",
        "year": 2022,
        "rating": 4.8, // updated rating
        "numberOfRatings": 401, // updated number of ratings (incremented by 1)
        "isbn": "1122334455",
        "genre": "Science Fiction",
        "pages": 350,
        "publisher": "Updated Publisher",
        "language": "German"
    }
    ```

### Search Books
- **Endpoint**: `/books/search`
- **Method**: `GET`
- **Description**: Searches books based on different criteria such as `title`, `author`, `year`, `rating`, `genre`, and more. Default sorting is by `title` in ascending order.
- **Query Parameters** (all optional, if not provided, all books are returned):

  | Parameter                  |     Type      | Description                                                                                                                                        |
  |----------------------------|:-------------:|----------------------------------------------------------------------------------------------------------------------------------------------------|
  | `title`                    |    String     | Filter books by title.                                                                                                                             |
  | `author`                   |    String     | Filter books by author.                                                                                                                            |
  | `year`                     |    Integer    | Filter books by publication year.                                                                                                                  |
  | `yearLimit`                |    Integer    | Set a limit for the year (min or max).                                                                                                             |
  | `yearDirection`            |   Direction   | Specifies the direction of `yearLimit` ("up" or "down").                                                                                           |
  | `startYear`                |    Integer    | Start year for a range filter.                                                                                                                     |
  | `endYear`                  |    Integer    | End year for a range filter.                                                                                                                       |
  | `rating`                   |    Double     | Filter books by rating.                                                                                                                            |
  | `ratingLimit`              |    Double     | Set a limit for rating (min or max).                                                                                                               |
  | `ratingDirection`          |   Direction   | Specifies the direction of `ratingLimit` ("up" or "down").                                                                                         |
  | `minRating`                |    Double     | Minimum rating for a range filter.                                                                                                                 |
  | `maxRating`                |    Double     | Maximum rating for a range filter.                                                                                                                 |
  | `numberOfRatings`          |    Integer    | Filter books by the number of ratings.                                                                                                             |
  | `numberOfRatingsLimit`     |    Integer    | Set a limit for the number of ratings (min or max).                                                                                                |
  | `numberOfRatingsDirection` |   Direction   | Specifies the direction of `numberOfRatingsLimit` ("up" or "down").                                                                                |
  | `minNumberOfRatings`       |    Integer    | Minimum number of ratings for a range filter.                                                                                                      |
  | `maxNumberOfRatings`       |    Integer    | Maximum number of ratings for a range filter.                                                                                                      |
  | `isbn`                     |    String     | Filter books by ISBN.                                                                                                                              |
  | `genre`                    |    String     | Filter books by genre.                                                                                                                             |
  | `pages`                    |    Integer    | Filter books by number of pages.                                                                                                                   |
  | `pagesLimit`               |    Integer    | Set a limit for the number of pages (min or max).                                                                                                  |
  | `minPages`                 |    Integer    | Minimum number of pages for a range filter.                                                                                                        |
  | `maxPages`                 |    Integer    | Maximum number of pages for a range filter.                                                                                                        |
  | `pagesDirection`           |   Direction   | Specifies the direction of `pagesLimit` ("up" or "down").                                                                                          |
  | `publisher`                |    String     | Filter books by publisher.                                                                                                                         |
  | `language`                 |    String     | Filter books by publication language.                                                                                                              |
  | `sortBy`                   | BookSortField | Fields to sort by: "title", "author", "year", "rating", "numberOfRatings", "isbn", "genre", "pages", "publisher", "language". Defaults to "title". |
  | `sortOrder`                |   SortOrder   | Specifies the order of sorting ("asc" or "desc"). Default is "asc".                                                                                |

#### Filter Books by Title
- **Endpoint**: `/books/search?title=light`
- **Method**: `GET`
- **Description**: Returns books with a title containing the specified keyword (case-insensitive). 
- **Query Parameters**:
    - `title`
- **Response Example**:
    ```json
    [
        {
        "id": 57,
        "title": "All the Light We Cannot See",
        "author": "Anthony Doerr",
        "year": 2014,
        "rating": 4.8,
        "numberOfRatings": 3150,
        "isbn": "978-1501173219",
        "genre": "Historical Fiction",
        "pages": 531,
        "publisher": "Scribner",
        "language": "English"
        },
        {
        "id": 74,
        "title": "All the Light We Cannot See",
        "author": "Anthony Doerr",
        "year": 2014,
        "rating": 4.8,
        "numberOfRatings": 1889,
        "isbn": "978-1501173219",
        "genre": "Historical Fiction",
        "pages": 531,
        "publisher": "Scribner",
        "language": "English"
        },
        ... // more books
    ]
    ```

#### Filter Books by Rating
- **Endpoint**: `/books/search?rating=4.9`
- **Method**: `GET`
- **Description**: Returns books with a specific rating.
- **Query Parameters**:
    - `rating`
- **Response Example**:
```json
  [
      {
      "id": 56,
      "title": "Atomic Habits",
      "author": "James Clear",
      "year": 2018,
      "rating": 4.9,
      "numberOfRatings": 4300,
      "isbn": "978-0735211292",
      "genre": "Self-help",
      "pages": 320,
      "publisher": "Avery",
      "language": "English"
      },
      {
      "id": 58,
      "title": "Becoming",
      "author": "Michelle Obama",
      "year": 2018,
      "rating": 4.9,
      "numberOfRatings": 3200,
      "isbn": "978-1524763138",
      "genre": "Memoir",
      "pages": 448,
      "publisher": "Crown",
      "language": "English"
      },
      ... // more books
  ]
```

#### Sort Books below/above a specified year
- **Endpoint**: `/books/search?yearLimit=2000&yearDirection=down`
- **Method**: `GET`
- **Description**: Returns books published before/after a specified year. 
  - When `yearDirection` is set to `down`, books published before the specified year are returned.
  - When set to `up`, books published after the specified year are returned.
  - If `yearDirection` is not provided by default acts as `up`.
- **Query Parameters**:
    - `yearLimit`
    - `yearDirection`
- **Response Example**:
    ```json
    [
      {
        "id": 2,
        "title": "1984",
        "author": "George Orwell",
        "year": 1949,
        "rating": 4.7,
        "numberOfRatings": 1005,
        "isbn": "978-0451524935",
        "genre": "Dystopian",
        "pages": 328,
        "publisher": "Secker & Warburg",
        "language": "English"
      },
      {
          "id": 16,
          "title": "Anna Karenina",
          "author": "Leo Tolstoy",
          "year": 1877,
          "rating": 4.5,
          "numberOfRatings": 723,
          "isbn": "978-0143035008",
          "genre": "Romance",
          "pages": 864,
          "publisher": "The Russian Messenger",
          "language": "Russian"
      },
      ... // more books
    ]
    ```
  
#### Filter Books by rating within a range
- **Endpoint**: `/books/search?minRating=4&maxRating=4.2`
- **Method**: `GET`
- **Description**: Returns books with a rating within a specified range.
    - If only `minRating` is provided, books with a rating greater than or equal to the specified value are returned.
    - If only `maxRating` is provided, books with a rating less than or equal to the specified value are returned.
- **Query Parameters**:
    - `minRating`
    - `maxRating`
- **Response Example**:
```json
    [
      {
        "id": 18,
        "title": "Dracula",
        "author": "Bram Stoker",
        "year": 1897,
        "rating": 4.2,
        "numberOfRatings": 365,
        "isbn": "978-0486411095",
        "genre": "Horror",
        "pages": 418,
        "publisher": "Archibald Constable and Company",
        "language": "English"
      },
      {
        "id": 30,
        "title": "Lolita",
        "author": "Vladimir Nabokov",
        "year": 1955,
        "rating": 4.2,
        "numberOfRatings": 56,
        "isbn": "978-0679723165",
        "genre": "Literary Fiction",
        "pages": 336,
        "publisher": "Vintage",
        "language": "English"
      },
      ... // more books
    ]
  ```

#### Combine Filters
- **Endpoint**: `/books/search?title=the&language=french&minRating=4.5&sortBy=numberOfRatings&sortOrder=desc`
- **Method**: `GET`
- **Description**: Combines multiple filters to search for books.
- **Query Parameters**: all parameters.
- **Response Example**:
    ```json
    [
      {
        "id": 52,
        "title": "The Little Prince",
        "author": "Antoine de Saint-Exupéry",
        "year": 1943,
        "rating": 4.8,
        "numberOfRatings": 2150,
        "isbn": "978-0156012195",
        "genre": "Fable",
        "pages": 96,
        "publisher": "Reynal & Hitchcock",
        "language": "French"
      },
      {
        "id": 20,
        "title": "The Count of Monte Cristo",
        "author": "Alexandre Dumas",
        "year": 1844,
        "rating": 4.8,
        "numberOfRatings": 111,
        "isbn": "978-0140449266",
        "genre": "Adventure",
        "pages": 1276,
        "publisher": "Penguin Classics",
        "language": "French"
      },
      ... // more books
    ]
    ```

## Error Handling
- **400 Bad Request**:
    - Returned when the request contains invalid data or parameters.
    - Specific message for `rating` parameter: "Invalid value for rating. Rating must be an integer between 1 and 5."
    - If an enum parameter (e.g., `BookSortField`, `Direction`, or `SortOrder`) has an invalid value, a message will be returned indicating the invalid value and the available options.

- **404 Not Found**:
    - Returned when the requested book does not exist.

- **500 Internal Server Error**:
    - Returned when there is a server-side error. A general error message will be logged, and the response will indicate an internal error occurred.
