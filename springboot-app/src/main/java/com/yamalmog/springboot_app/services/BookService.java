package com.yamalmog.springboot_app.services;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yamalmog.springboot_app.db.dbManager;
import com.yamalmog.springboot_app.models.Book;

@Service
public class BookService {
    @Autowired
    private final dbManager dbManager;
    
    // Constructor 
    public BookService(dbManager dbManager){
        this.dbManager = dbManager;
    }

    public void addBook(Book book){
        dbManager.addBook(book);
    }

    public List<Book> getAllBooks(){
        return dbManager.getAllBooks();
    }

    public Book getBookById(int id){
        return dbManager.GetSpecificBook(id);
    }

    public void deleteBook(int id){
        dbManager.deleteBook(id);
    }
}
