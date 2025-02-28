package com.yamalmog.springboot_app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import com.yamalmog.springboot_app.db.dbManager;
import com.yamalmog.springboot_app.exceptions.BooksAppException;
import com.yamalmog.springboot_app.models.Book;

@Service
public class BookService {
    @Autowired
    private final dbManager dbManager;
    private final JdbcTemplate jdbcTemplate;

    // Constructor 
    public BookService(dbManager dbManager, JdbcTemplate jdbcTemplate){
        this.dbManager = dbManager;
        this.jdbcTemplate = jdbcTemplate;
    }


    // Add new book
    public ResponseEntity<String> addBook(Book book){
        Integer count_on_book_id = dbManager.check_if_id_exist(book.getId(), "books");

        if (count_on_book_id != null && count_on_book_id > 0) {
            throw new BooksAppException(BooksAppException.ErrorType.BOOK_ID_ALREADY_EXIST, "Book With Id: "+ book.getId() + " already exists.");
        }

        String query = "INSERT INTO books(id, title, author, is_available) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(query, book.getId(), book.getTitle(), book.getAuthor(), book.isAvailable());
        return ResponseEntity.status(HttpStatus.CREATED).body("Book added successfully.");
    }

    // Get all books
    public List<Book> getAllBooks(){
        String query = "SELECT * FROM books";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Book.class));
    }

    // Get specific book
    public Book getBookById(int book_id){
        String query = "SELECT * FROM books WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(Book.class), book_id); //BeanPropertyRowMapper simplifies the process of mapping rows of a ResultSet to Java beans.
        } catch (EmptyResultDataAccessException e) {
            throw new BooksAppException(BooksAppException.ErrorType.BOOK_NOT_FOUND, "Book With Id: "+ book_id + " does not exist.");
        }       
    }

    // Delete a book
    public ResponseEntity<String> deleteBook(int book_id){
        String query = "DELETE FROM books WHERE id = ?";
        jdbcTemplate.update(query, book_id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Book with id: " + book_id + " has been deleted.");
    }
    
}

