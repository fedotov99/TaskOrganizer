package com.company;
import java.util.Map;
import java.util.HashMap;

public abstract class User {
    private int userID;
    private static int counter = 1;
    private String name;
    protected HashMap<Integer, Task> localUserTaskList = new HashMap<Integer, Task>();

    User() {
        this.userID = counter++;
        this.name = "User" + userID;
        // localUserTaskList = new HashMap<Integer, Task>(); // there was a bug
    }

    User(String name) {
        this.userID = counter++;
        this.name = name;
    }

    public void addTask(Task task) {  // bug
        Integer i = task.getTaskID();
        localUserTaskList.put(i, task);
        //localUserTaskList.putIfAbsent(i, task);
    }

    public abstract void completeTask (int id, String report);

    public void deleteTask(int id) {
        if (localUserTaskList.containsKey(id)) {
            localUserTaskList.remove(id);
        }
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
