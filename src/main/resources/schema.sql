CREATE TABLE book (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    publication_year INT NOT NULL,
    rating DOUBLE,
    number_of_ratings INT DEFAULT 0,
    isbn VARCHAR(20) NOT NULL,
    genre VARCHAR(100) NOT NULL,
    pages INT NOT NULL,
    publisher VARCHAR(255) NOT NULL,
    publication_language VARCHAR(50) NOT NULL
);
