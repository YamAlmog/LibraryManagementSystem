package com.yamalmog.springboot_app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yamalmog.springboot_app.models.User;
import com.yamalmog.springboot_app.db.dbManager;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private final dbManager dbManager;

    // Constractor
    public UserService(dbManager dbManager){
        this.dbManager = dbManager;
    }

    public void addUser(User user){
        dbManager.addUser(user);
    }

    public List<User> getAllUsers(){
        return dbManager.getAllUsers();
    }

    public User getUserById(int user_id){
        return dbManager.getUserById(user_id);
    }

    public void deleteUser(int user_id){
        dbManager.deleteUser(user_id);
    }

}
