package com.company;
import java.util.Map;
import java.util.HashMap;

public abstract class User {
    private int userID;
    private static int counter = 1;
    private String name;
    protected HashMap<Integer, Task> localUserTaskList = new HashMap<Integer, Task>();

    User() {
        userID = counter++;
        name = "User" + userID;
        // localUserTaskList = new HashMap<Integer, Task>(); // there was a bug
    }

    User(String _name) {
        userID = counter++;
        name = _name;
    }

    public void addTask(Task task) {  // bug
        Integer i = task.getTaskID();
        localUserTaskList.put(i, task);
        //localUserTaskList.putIfAbsent(i, task);
    }

    public abstract void completeTask (int _id, String _report);

    public void deleteTask(int _id) {
        if (localUserTaskList.containsKey(_id)) {
            localUserTaskList.remove(_id);
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
