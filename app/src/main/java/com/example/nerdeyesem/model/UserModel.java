package com.example.nerdeyesem.model;

public class UserModel {
    private final String email;
    private final String password;

    public UserModel(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
