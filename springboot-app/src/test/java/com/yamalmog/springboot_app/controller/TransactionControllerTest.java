package com.yamalmog.springboot_app.controller;

import com.yamalmog.springboot_app.models.Book;
import com.yamalmog.springboot_app.models.User;
import com.yamalmog.springboot_app.models.Transaction;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    void addBook(int id){
        Book book = new Book(id, "Test Book", "Test Author", true);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Book> request = new HttpEntity<>(book, headers);

        // Send HTTP POST request to add the book
        ResponseEntity<String> response = restTemplate.postForEntity("/books", request, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    void deleteBook(int id){
        restTemplate.delete("/books/" + id);
    }
    
    void addUser(int id, String name, String email){
        User user = new User(id, name, email);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request = new HttpEntity<>(user, headers);

        // Send HTTP Post request to create user
        ResponseEntity<String> response = restTemplate.postForEntity("/users", request, String.class);
        System.out.println("-----------------------------<>---------------------------");
        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());  // Print response body
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    void deleteUser(int id){
        ResponseEntity<String> deleteResponse = restTemplate.exchange("/users/"+id, HttpMethod.DELETE, null, String.class);
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
    }
    
    @Test
    void testAddAndGetTransaction(){
        // Clean DB
        deleteBook(1);
        deleteUser(1);

        // create book and user
        addBook(1);
        addUser(1, "Yam", "Yam11@mail.com");
        // Create a new Transaction
        Transaction transaction = new Transaction(1,1,"borrow");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Transaction> request = new HttpEntity<>(transaction, headers);
        // Send HTTP POST request to add the transaction
        ResponseEntity<String> response = restTemplate.postForEntity("/transactions", request, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Get transactions
        ResponseEntity<Transaction[]> getResponse = restTemplate.getForEntity("/transactions", Transaction[].class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertTrue(getResponse.getBody().length >= 1);

        // Clean DB
        deleteBook(1);
        deleteUser(1);
    }

    @Test
    void testGetTransactionByBookAndByUser(){
        // Clean db
        deleteBook(1);
        deleteUser(1);
        deleteBook(2);
        deleteUser(2);


        // Create books and users
        addBook(1);
        addUser(1,"Yam", "Yam11@mail.com");
        addBook(2);
        addUser(2, "Roi", "Roiroi@mail.com");
        // Create a new Transaction
        Transaction transaction1 = new Transaction(1,1,"borrow");
        Transaction transaction2 = new Transaction(2,2,"borrow");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Transaction> request1 = new HttpEntity<>(transaction1, headers);
        HttpEntity<Transaction> request2 = new HttpEntity<>(transaction2, headers);
        
        // Send HTTP POST request to add the transaction
        ResponseEntity<String> response1 = restTemplate.postForEntity("/transactions", request1, String.class);
        assertEquals(HttpStatus.CREATED, response1.getStatusCode());
        ResponseEntity<String> response2 = restTemplate.postForEntity("/transactions", request2, String.class);
        assertEquals(HttpStatus.CREATED, response2.getStatusCode());


        // Get transactions by book
        ResponseEntity<Transaction[]> getResponse = restTemplate.getForEntity("/transactions/book/1", Transaction[].class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertTrue(getResponse.getBody().length >= 1);
        

        // Get transactions by user
        ResponseEntity<Transaction[]> getResponseByUser = restTemplate.getForEntity("/transactions/user/2", Transaction[].class);
        assertEquals(HttpStatus.OK, getResponseByUser.getStatusCode());
        assertNotNull(getResponseByUser.getBody());
        assertTrue(getResponseByUser.getBody().length >= 1);
        

        // Clean DB
        deleteBook(1);
        deleteUser(1);
        deleteBook(2);
        deleteUser(2);
    }


    // ----------------- Negative Checks ---------------
    // Get Transaction when User not found
    @Test
    void testGetTransactionWhenUserNotFound(){
         // Send GET request
         ResponseEntity<String> response = restTemplate.getForEntity("/user/" + 11, String.class);

         // Assert response status is 404 NOT FOUND
         assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    // Get Transaction when Book not found
    @Test
    void testGetTransactionWhenBookNotFound(){
         // Send GET request
         ResponseEntity<String> response = restTemplate.getForEntity("/book/" + 30, String.class);

         // Assert response status is 404 NOT FOUND
         assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    // Add Transaction with Book that doesn't exist
    @Test
    void testAddTransactionWithNotExistBook(){
        // Clean DB
        deleteBook(2);
        deleteUser(2);

        // Create only user
        addUser(2, "Roi", "Roiroi@mail.com");
        
        Transaction transaction = new Transaction(2,2,"borrow");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Transaction> request = new HttpEntity<>(transaction, headers);
        
        // Send HTTP POST request to add the transaction Not existing book
        ResponseEntity<String> response = restTemplate.postForEntity("/transactions", request, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Clean DB
        deleteUser(2);
    }


    // Add Transaction with User that doesn't exist
    @Test
    void testAddTransactionWithNotExistUser(){
        // Clean DB
        deleteBook(2);
        deleteUser(2);

        // Create only book
        addBook(2);
        
        Transaction transaction = new Transaction(2,2,"borrow");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Transaction> request = new HttpEntity<>(transaction, headers);
        
        // Send HTTP POST request to add the transaction Not existing book
        ResponseEntity<String> response = restTemplate.postForEntity("/transactions", request, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Clean DB
        deleteBook(2);
    }


    // Add Transaction with wrong status
    @Test
    void testAddTransactionWithWrongStatus(){
        // Clean DB
        deleteBook(2);
        deleteUser(2);


        addBook(2);
        addUser(2, "Roi", "Roiroi@mail.com");
        
        Transaction transaction = new Transaction(2,2,"buy");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Transaction> request = new HttpEntity<>(transaction, headers);
        
        // Send HTTP POST request to add the transaction with wrong status
        ResponseEntity<String> response = restTemplate.postForEntity("/transactions", request, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Clean DB
        deleteBook(2);
        deleteUser(2);
    }


    //  Book is not available
    @Test
    void testAddTransactionWithUnavailableBook(){
        // Clean DB
        deleteBook(2);
        deleteUser(2);
        deleteUser(4);

        addBook(2);
        addUser(2, "Roi", "Roiroi@mail.com");
        addUser(4, "Yaron", "Yaron@mail.com");
        Transaction transaction = new Transaction(2,2,"borrow");
        Transaction transaction1 = new Transaction(2,4,"borrow"); // Transaction to add Book 2 that already borrowed
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Transaction> request = new HttpEntity<>(transaction, headers);
        HttpEntity<Transaction> request1 = new HttpEntity<>(transaction1, headers);
        
        ResponseEntity<String> response = restTemplate.postForEntity("/transactions", request, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        
        // Send HTTP POST request to add the transaction with Unavailable book
        ResponseEntity<String> response1 = restTemplate.postForEntity("/transactions", request1, String.class);
        assertEquals(HttpStatus.CONFLICT, response1.getStatusCode());
        System.out.println("-----------------------------<>---------------------------");
        System.out.println("Response status: " + response1.getStatusCode());
        System.out.println("Response body: " + response1.getBody());  // Print response body

        // Clean DB
        deleteBook(2);
        deleteUser(2);
        deleteUser(4);
    }

}
