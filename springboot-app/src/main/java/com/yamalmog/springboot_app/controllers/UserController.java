package com.yamalmog.springboot_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yamalmog.springboot_app.services.UserService;
import com.yamalmog.springboot_app.models.User;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping
    public void addUser(@RequestBody User user){
        userService.addUser(user);
    }
    
    @GetMapping("/{id}")
    public User getUserById(@PathVariable int user_id){
        return userService.getUserById(user_id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int user_id){
        userService.deleteUser(user_id);
    }


}
