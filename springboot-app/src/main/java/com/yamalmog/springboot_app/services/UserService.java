package com.yamalmog.springboot_app.services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import com.yamalmog.springboot_app.models.User;
import com.yamalmog.springboot_app.db.dbManager;
import com.yamalmog.springboot_app.exceptions.UserAppException;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private final dbManager dbManager;
    private final JdbcTemplate jdbcTemplate;

    // Constractor
    public UserService(dbManager dbManager, JdbcTemplate jdbcTemplate){
        this.dbManager = dbManager;
        this.jdbcTemplate = jdbcTemplate;
    }


    // Add new user
    public ResponseEntity<String> addUser(User user){
        Integer count_on_user_id = dbManager.check_if_id_exist(user.getId(), "users");
    
        if(count_on_user_id != null && count_on_user_id > 0){
            throw new UserAppException(UserAppException.ErrorType.USER_ID_ALREADY_EXIST, "User with Id: " + user.getId()+ " already exists.");
        }
        String query = "INSERT INTO users(id, name, email) VALUES (?,?,?)";
        jdbcTemplate.update(query, user.getId(), user.getName(), user.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body("User added successfully.");
    }

    // Get all users
    public List<User> getAllUsers(){
        String query = "SELECT * FROM users";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(User.class));
    }

    // Get a specific user
    public User getUserById(int user_id){
        String get_user_query = "SELECT * FROM users WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(get_user_query, new BeanPropertyRowMapper<>(User.class), user_id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserAppException(UserAppException.ErrorType.USER_NOT_FOUND, "User with Id: " + user_id + " does not exist.");
        }
    }

    // Delete user
    public ResponseEntity<String> deleteUser(int user_id){
        String delete_user_query = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(delete_user_query, user_id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User with id: " + user_id + " has been deleted.");
    }

}
