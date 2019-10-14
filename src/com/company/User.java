package com.company;
import java.util.Map;
import java.util.HashMap;

public abstract class User {
    private int userID;
    private static int counter = 1;
    private String name;
    protected Map<Integer, Task> localUserTaskList = new HashMap<Integer, Task>();

    User() {
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
}
