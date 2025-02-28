package com.yamalmog.springboot_app.controller;

import com.yamalmog.springboot_app.models.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testAddUserGetUsers(){
        // Setup the db before add the user
        restTemplate.delete("/users/1");
        
        // Create a new user
        User user = new User(1, "Tomas A", "Tomas@mail.com");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request = new HttpEntity<>(user, headers);

        // Send HTTP Post request to create user
        ResponseEntity<String> response = restTemplate.postForEntity("/users", request, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Get users
        ResponseEntity<User[]> getResponse = restTemplate.getForEntity("/users", User[].class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertTrue(getResponse.getBody().length > 0);

        // Delete user by id, clean DB
        ResponseEntity<String> deleteResponse = restTemplate.exchange("/users/1", HttpMethod.DELETE, null, String.class);
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
    }


    @Test
    void testGetUserById(){
        // Setup the db before add the user
        restTemplate.delete("/users/2");
        
        // Create a new user
        User user = new User(2, "Oren Ben-Ari", "Orenben@mail.com");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request = new HttpEntity<>(user, headers);

        // Send HTTP Post request to create user
        ResponseEntity<String> response = restTemplate.postForEntity("/users", request, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Get user with id = 2
        ResponseEntity<User> getResponse = restTemplate.getForEntity("/users/2", User.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode()); 
        assertNotNull(getResponse.getBody());
        assertEquals(2, getResponse.getBody().getId());
        assertEquals("Oren Ben-Ari", getResponse.getBody().getName());
        assertEquals("Orenben@mail.com", getResponse.getBody().getEmail());

        // Delete user by id, clean DB
        ResponseEntity<String> deleteResponse = restTemplate.exchange("/users/2", HttpMethod.DELETE, null, String.class);
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
    }


    // Negative checks
    @Test
    void testAddUserWithExistId(){
        // Create a new user
        User user = new User(1, "Tomas A", "Tomas@mail.com");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request = new HttpEntity<>(user, headers);

        // Send HTTP Post request to create user
        ResponseEntity<String> response = restTemplate.postForEntity("/users", request, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());


        // Create a new user
        User user1 = new User(1, "Tim Bim", "Tim10@mail.com");
        HttpHeaders headers1 = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request1 = new HttpEntity<>(user1, headers1);

        // Send HTTP Post request to create user
        ResponseEntity<String> response1 = restTemplate.postForEntity("/users", request1, String.class);
        assertEquals(HttpStatus.CONFLICT, response1.getStatusCode());
    }

    @Test
    void testGetNotExistUser(){
        // Setup the db before add the user
        restTemplate.delete("/users/2");

        // Get user with id = 2
        ResponseEntity<User> getResponse = restTemplate.getForEntity("/users/2", User.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode()); 
        
    }
}
