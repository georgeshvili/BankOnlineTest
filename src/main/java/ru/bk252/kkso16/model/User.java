package ru.bk252.kkso16.model;

public class User {

    private String email;
    private String name;
    private String passwordHash;
    private String role;

    public User(String email, String name, String passwordHash, String role) {
        this.email = email;
        this.name = name;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPasswordHash() {
        return this.passwordHash;
    }
}