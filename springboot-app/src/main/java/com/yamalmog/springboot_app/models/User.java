package com.yamalmog.springboot_app.models;

public class User {
    private int id;
    private String name;
    private String email;

    public User(int user_id, String user_name, String user_email){
        this.id = user_id;
        this.name = user_name;
        this.email = user_email;
    }

    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
}