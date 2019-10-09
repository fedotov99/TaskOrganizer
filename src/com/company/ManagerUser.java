package com.company;

import java.util.HashMap;
import java.util.Map;

public class ManagerUser extends User {
    // each manager will have one or more sub.
    private HashMap<Integer, SubordinateUser>  subordinateList = new HashMap<Integer, SubordinateUser>();
    private HashMap<Integer, Task> uncheckedTasksList = new HashMap<Integer, Task>(); // requests from sub.

    ManagerUser(String _name) {
        super(_name);
        // subordinateList = new HashMap<Integer, SubordinateUser>();
        // uncheckedTasksList = new HashMap<Integer, Task>();
    }

    @Override
    public void completeTask (int id, String report) {            // implements abstract method in User
        if (localUserTaskList.get(id) != null) {
            localUserTaskList.get(id).setReport(report);
            localUserTaskList.get(id).setCompleted(true);
            localUserTaskList.remove(id);
        }
    }

    public void addSubordinate(SubordinateUser su) {
        subordinateList.putIfAbsent(su.getUserID(), su);
    }

    public void addToUncheckedTasksList(Task task) {  // this method will be used by any subordinate who wants to send task request
        uncheckedTasksList.putIfAbsent(task.getTaskID(), task);
    }

    public void assignTaskToSubordinate(Task task, SubordinateUser su) {
        if (localUserTaskList.containsKey(task) && subordinateList.containsKey(su)) { // can assign only OUR employee
            su.addTask(task);
        }
    }

    public int getSubordinatesSize() {
        return subordinateList.size();
    }
}
