package com.company;

import java.util.HashMap;
import java.util.Map;

public class ManagerUser extends User {
    private HashMap<Integer, SubordinateUser>  subordinateList; // each manager will have one or more sub.
    private HashMap<Integer, Task> uncheckedTasksList; // requests from sub.

    ManagerUser(String _name) {
        super(_name);
        subordinateList = new HashMap<Integer, SubordinateUser>();
        uncheckedTasksList = new HashMap<Integer, Task>();
    }

    @Override
    public void completeTask (int _id, String _report) {            // implements abstract method in User
        if (localUserTaskList.get(_id) != null) {
            localUserTaskList.get(_id).setReport(_report);
            localUserTaskList.get(_id).setCompleted(true);
            localUserTaskList.remove(_id);
        }
    }

    public void addSubordinate(SubordinateUser _su) {
        subordinateList.putIfAbsent(_su.getUserID(), _su);
    }

    public void addToUncheckedTasksList(Task _task) {  // this method will be used by any subordinate who wants to send task request
        uncheckedTasksList.putIfAbsent(_task.getTaskID(), _task);
    }

    public void assignTaskToSubordinate(Task _task, SubordinateUser _su) {
        if (localUserTaskList.containsKey(_task) && subordinateList.containsKey(_su)) { // can assign only OUR employee
            _su.addTask(_task);
        }
    }

    public int getSubordinatesSize() {
        return subordinateList.size();
    }
}
