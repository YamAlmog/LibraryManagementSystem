package com.yamalmog.springboot_app.db;

import java.util.List;

import com.yamalmog.springboot_app.exceptions.BooksAppException;
import com.yamalmog.springboot_app.exceptions.UserAppException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.yamalmog.springboot_app.models.Book;
import com.yamalmog.springboot_app.models.User;

@Component
public class dbManager {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public dbManager(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


    // Initializes tables on application startup
    @EventListener(ContextRefreshedEvent.class)
    public void initializeDatabase() {
        createBooksTable();
        createUsersTable();
        createTransactionsTable();
    }

    private void createBooksTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS books (
                id INT PRIMARY KEY,
                title VARCHAR(255) NOT NULL,
                author VARCHAR(255) NOT NULL,
                is_available BOOLEAN DEFAULT TRUE);"""; 
        jdbcTemplate.execute(sql);
    }

    private void createUsersTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id INT PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                email VARCHAR(255) UNIQUE NOT NULL);""";
        jdbcTemplate.execute(sql);
    }

    private void createTransactionsTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS transactions (
                id INT PRIMARY KEY,
                book_id INT NOT NULL,
                user_id INT NOT NULL,
                status VARCHAR(255),
                transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                CONSTRAINT fk_book FOREIGN KEY (book_id) REFERENCES books(id),
                CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id));""";
        jdbcTemplate.execute(sql);
    }


    // ---------------- General helper methods ----------------
    

    // ----------------------- Book Methods -------------------
    // Add new book
    public void addBook(Book book){
        String check_query = "SELECT COUNT(*) FROM books WHERE id = ?";
        Integer count_on_book_id = jdbcTemplate.queryForObject(check_query, Integer.class, book.getId());

        if (count_on_book_id != null && count_on_book_id > 0) {
            throw new BooksAppException(BooksAppException.ErrorType.BOOK_ID_ALREADY_EXIST, "Book With Id: "+ book.getId() + " already exists.");
        }

        String query = "INSERT INTO books(id, title, author, is_available) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(query, book.getId(), book.getTitle(), book.getAuthor(), book.isAvailable());
    }

    // Get all books
    public List<Book> getAllBooks(){
        String query = "SELECT * FROM books";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Book.class));
    }

    // Get specific book
    public Book GetSpecificBook(int book_id){
        String query = "SELECT * FROM books WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(Book.class), book_id); //BeanPropertyRowMapper simplifies the process of mapping rows of a ResultSet to Java beans.
        } catch (EmptyResultDataAccessException e) {
            throw new BooksAppException(BooksAppException.ErrorType.BOOK_NOT_FOUND, "Book With Id: "+ book_id + " does not exist.");
        }       
    }

    // Delete a book
    public void deleteBook(int book_id){
        String query = "DELETE FROM books WHERE id = ?";
        jdbcTemplate.update(query, book_id);
    }
    
    // ----------------------- User Methods -------------------
    // Add new user
    public void addUser(User user){
        String search_user_by_id = "SELECT * FROM users WHERE id = ?";
        Integer count_on_user_id = jdbcTemplate.queryForObject(search_user_by_id, Integer.class, user.getId());
    
        if(count_on_user_id != null && count_on_user_id > 0){
            throw new UserAppException(UserAppException.ErrorType.USER_ID_ALREADY_EXIST, "User with Id: " + user.getId()+ " already exists.");
        }
        String query = "INSERT INTO users(id, name, email) VALUES (?,?,?)";
        jdbcTemplate.update(query, user.getId(), user.getName(), user.getEmail());
    }

    // Get all users
    public List<User> getAllUsers(){
        String query = "SELECT * FROM users";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(User.class));
    }

    // Get a specific user
    public User getUserById(int id){
        String check_query
    }

    // Delete user
    public void deleteUser(int id){

    }

}
