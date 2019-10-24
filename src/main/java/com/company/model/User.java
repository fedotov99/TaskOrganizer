package com.company.model;
import org.springframework.data.annotation.Id;

import java.util.Map;
import java.util.HashMap;

public abstract class User {
    @Id
    private String userID;
    private static int counter = 1;
    private String name;
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

    public String getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
