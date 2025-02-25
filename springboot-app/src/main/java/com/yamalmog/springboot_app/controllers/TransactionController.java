package com.yamalmog.springboot_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.yamalmog.springboot_app.services.TransactionService;
import com.yamalmog.springboot_app.models.Transaction;
import java.util.List;



@RestController  // Marks this as a REST API controller
@RequestMapping("/transactions")  // Base URL: /transactions
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<String> addTransaction(@RequestBody Transaction transaction){
        return transactionService.addTransaction(transaction);
    }

    @GetMapping
    public List<Transaction> getAllTransactions(){
        return transactionService.getAllTransactions();
    }

    @GetMapping("/user/{userId}")
    public List<Transaction> getTransactionByUserId(@PathVariable int userId){
        return transactionService.getTransactionsByUser(userId);
    }

    @GetMapping("/book/{bookId}")
    public List<Transaction> getTransactionByBookId(@PathVariable int bookId){
        return transactionService.getTransactionsByBook(bookId);
    }

    
}
