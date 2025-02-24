package com.yamalmog.springboot_app.models;


import java.time.LocalDateTime;


public class Transaction {
    
    private int id;
    private int bookId;
    private int userId;
    private String status;
    private LocalDateTime transactionDate;

    // Default constructor (for JSON deserialization)
    public Transaction() {}

    public Transaction(int bookId, int userId, String status){
        this.bookId = bookId;
        this.userId = userId;
        this.status = status;
        this.transactionDate = LocalDateTime.now();  // Current timestamp
    }
    
    // Getters and Setters
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    public int getBookId(){
        return bookId;
    }
    public void setBookId(int bookId){
        this.bookId = bookId;
    }

    public int getUserId(){
        return userId;
    }
    public void setUserId(int userId){
        this.userId = userId;
    }

    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }

    public LocalDateTime getTransactionDate(){
        return transactionDate;
    }
    public void setTransactionDate(LocalDateTime transactionDate){
        this.transactionDate = transactionDate;
    }
}
