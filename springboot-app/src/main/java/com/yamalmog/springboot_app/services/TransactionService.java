package com.yamalmog.springboot_app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.yamalmog.springboot_app.models.Transaction;

import org.springframework.jdbc.core.JdbcTemplate;

import com.yamalmog.springboot_app.db.dbManager;
import com.yamalmog.springboot_app.exceptions.BooksAppException;
import com.yamalmog.springboot_app.exceptions.UserAppException;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.yamalmog.springboot_app.exceptions.TransactionAppException;


@Service
public class TransactionService{
    @Autowired
    private final dbManager dbManager;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TransactionService(JdbcTemplate jdbcTemplate, dbManager dbManager){
        this.jdbcTemplate = jdbcTemplate;
        this.dbManager = dbManager;
    }

    // Add a new transaction
    public ResponseEntity<String> addTransaction(Transaction transaction) {
        int bookId = transaction.getBookId();
        int userId = transaction.getUserId();
        String status = transaction.getStatus();
        Integer count_on_book_id = dbManager.check_if_id_exist(bookId, "books");
        Integer count_on_user_id = dbManager.check_if_id_exist(userId, "users");
        try{
            if (count_on_book_id == null || count_on_book_id == 0) {
                throw new BooksAppException(BooksAppException.ErrorType.BOOK_NOT_FOUND, "Book With Id: "+ bookId + " does not exist.");
            }
            if (count_on_user_id == null || count_on_user_id == 0) {
                throw new UserAppException(UserAppException.ErrorType.USER_NOT_FOUND, "User With Id: "+ userId + " does not exist.");
            }
            if (!status.equals("borrow") && !status.equals("return")) {
                return ResponseEntity.badRequest().body("Invalid status. Must be 'borrow' or 'return'.");
            }

            // Check if the book is available before borrowing
            boolean is_available_book = dbManager.check_if_book_available(bookId);
            if (status.equals("borrow") && is_available_book == false){
                throw new TransactionAppException(TransactionAppException.ErrorType.UNAVAILABLE_BOOK, "Book With Id: "+ bookId + " is Unavailable.");
            }

            String add_transaction_query = "INSERT INTO transactions(book_id, user_id, status) VALUES (?,?,?)";
            jdbcTemplate.update(add_transaction_query, bookId, userId, status);
            
            String update_book_query = "UPDATE books SET is_available = ? WHERE id = ?";
            boolean newAvailability = switch (status) {
                case "borrow" -> false;
                case "return" -> true;
                default -> throw new IllegalArgumentException("Invalid status: " + status);

            };
            jdbcTemplate.update(update_book_query, newAvailability, bookId);

            return ResponseEntity.status(HttpStatus.CREATED).body("Transaction added successfully.");

        } catch (BooksAppException | UserAppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (TransactionAppException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing transaction.");
        }
    }
                        

    // Get all transactions
    public List<Transaction> getAllTransactions() {
        String all_transaction_query = "SELECT * FROM transactions";
        return jdbcTemplate.query(all_transaction_query, new BeanPropertyRowMapper<>(Transaction.class));
    }
    

    // Get all transactions for a specific user
    public List<Transaction> getTransactionsByUser(int userId) {
        String get_by_user_query = "SELECT * FROM transactions WHERE user_id = ?";
        try {
            return jdbcTemplate.query(get_by_user_query, new BeanPropertyRowMapper<>(Transaction.class), userId);
        } catch (EmptyResultDataAccessException e) {
            throw new UserAppException(UserAppException.ErrorType.USER_NOT_FOUND, "Transaction with User Id: " + userId + " does not exist.");
        }
    }

    
    // Get all transactions for a specific book
    public List<Transaction> getTransactionsByBook(int bookId) {
        String get_by_book_query = "SELECT * FROM transactions WHERE book_id = ?";
        try {
            return jdbcTemplate.query(get_by_book_query, new BeanPropertyRowMapper<>(Transaction.class), bookId);
        } catch (EmptyResultDataAccessException e) {
            throw new BooksAppException(BooksAppException.ErrorType.BOOK_NOT_FOUND, "Transaction with Book Id: " + bookId + " does not exist.");
        }
    }

}
