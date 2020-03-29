package com.example.chatapp;

public class User {

    String id, username;

    public User() {
    }

    public User(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
