package com.example.amqp.core;

public class User {

    public User() {
    }

    public User(String name, String password) {
        setUserName(name);
        setPassword(password);
    }

    private String userName;
    private String password;
    private String fullName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        userName = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
