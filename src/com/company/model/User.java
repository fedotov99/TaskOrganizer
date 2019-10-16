package com.company.model;
import java.util.Map;
import java.util.HashMap;

public abstract class User {
    private int userID;
    private static int counter = 1;
    private String name;
    private Map<Integer, Task> localUserTaskList = new HashMap<Integer, Task>();

    public User() {
        this.userID = counter++;
        this.name = "User" + userID;
        // localUserTaskList = new HashMap<Integer, Task>(); // there was a bug
    }

    User(String name) {
        this.userID = counter++;
        this.name = name;
    }

    public int getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Integer, Task> getLocalUserTaskList() {
        return localUserTaskList;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", name='" + name + '\'' +
                '}';
    }
}
