package com.yamalmog.springboot_app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
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

    public Book getBookById(int book_id){
        return dbManager.GetSpecificBook(book_id);
    }

    public void deleteBook(int book_id){
        dbManager.deleteBook(book_id);
    }
}
