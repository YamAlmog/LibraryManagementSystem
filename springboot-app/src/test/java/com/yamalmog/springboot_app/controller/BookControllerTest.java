package com.yamalmog.springboot_app.controller;

import com.yamalmog.springboot_app.models.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testAddBookAndGetBooks() {

        // Create a new book
        Book book = new Book(20, "Test Book", "Test Author", true);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Book> request = new HttpEntity<>(book, headers);

        // Send HTTP POST request to add the book
        ResponseEntity<String> response = restTemplate.postForEntity("/books", request, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Send HTTP GET request to get all books
        ResponseEntity<Book[]> getResponse = restTemplate.getForEntity("/books", Book[].class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertTrue(getResponse.getBody().length > 0);

        // Ensure the database is clean
        ResponseEntity<String> getDeleteResponse = restTemplate.exchange("/books/20", HttpMethod.DELETE, null, String.class);
        assertEquals(HttpStatus.NO_CONTENT, getDeleteResponse.getStatusCode());
    }


    @Test
    void testGetBookById() {
        // Clean Database
        restTemplate.delete("/books/30");

        // Create a new book
        Book book = new Book(30, "Hello World", "Harry P", true);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Book> request = new HttpEntity<>(book, headers);

        // Send HTTP POST request to add the book
        ResponseEntity<String> response = restTemplate.postForEntity("/books", request, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Send HTTP GET request to get book by id
        ResponseEntity<Book> getResponse = restTemplate.getForEntity("/books/30", Book.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(30, getResponse.getBody().getId());
        assertEquals("Hello World", getResponse.getBody().getTitle());
        assertEquals("Harry P", getResponse.getBody().getAuthor());

        // Clean Database
        restTemplate.delete("/books/30");
    }


    // Negative Check - make sure it returns the exactly bad response i expect
    @Test
    void testAddExistingBookId() {
        // Add a new book
        Book book = new Book(1, "Test Book", "Test Author", true);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Book> request = new HttpEntity<>(book, headers);
        restTemplate.postForEntity("/books", request, String.class);

        // Try add book with existing id
        Book book1 = new Book(1, "Test Existing Book id", "Test Author", true);
        HttpHeaders headers1 = new HttpHeaders();
        headers1.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Book> request1 = new HttpEntity<>(book1, headers1);
        // Send HTTP POST request
        ResponseEntity<String> response1 = restTemplate.postForEntity("/books", request1, String.class);
        assertEquals(HttpStatus.CONFLICT, response1.getStatusCode());

        // Clean Database
        ResponseEntity<String> getDeleteResponse = restTemplate.exchange("/books/1", HttpMethod.DELETE, null, String.class);
        assertEquals(HttpStatus.NO_CONTENT, getDeleteResponse.getStatusCode());
    }

    @Test
    void testGetNotExistBook() {
        // Clean Database
        restTemplate.delete("/books/11");

        // Send HTTP GET request to get book by id that Does Not Exist
        ResponseEntity<Book> getResponse = restTemplate.getForEntity("/books/11", Book.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode()); 
    }
}  
