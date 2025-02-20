package com.yamalmog.springboot_app.models;

import java.time.LocalDateTime;

public class Transaction {
    private int id;
    private int bookId;
    private int userId;
    private LocalDateTime transactionTimeStemp;

    public Transaction(int transaction_id, int book_id, int user_id){
        this.id = transaction_id;
        this.bookId = book_id;
        this.userId = user_id;
        this.transactionTimeStemp = LocalDateTime.now();
    }
    
    public int getId(){
        return id;
    }
    public int getBookId(){
        return bookId;
    }
    public int getUserId(){
        return userId;
    }
    public LocalDateTime getTransactionTimeStemp(){
        return transactionTimeStemp;
    }
}
