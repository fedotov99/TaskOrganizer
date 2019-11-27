package com.company.model;
import org.springframework.data.annotation.Id;

import java.util.Map;
import java.util.HashMap;

public abstract class User {
    @Id
    private String userID;
    private static int counter = 1;
    private String name;
    private String email; // TODO: make unique
    private String password;
    private Map<String, Task> localUserTaskList = new HashMap<String, Task>();

    public User() {
        // this.userID = counter++;
        this.name = "User" + userID;
        // localUserTaskList = new HashMap<Integer, Task>(); // there was a bug
    }

    User(String name) {
        // this.userID = counter++;
        this.name = name;
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, Task> getLocalUserTaskList() {
        return localUserTaskList;
    }

    public void setLocalUserTaskList(Map<String, Task> localUserTaskList) {
        this.localUserTaskList = localUserTaskList;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", name='" + name + '\'' +
                '}';
    }
}
