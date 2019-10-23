package com.company.model;

import java.util.HashMap;
import java.util.Map;

public class ManagerUser extends User {
    // each manager will have one or more sub.
    private Map<Integer, SubordinateUser>  subordinateList = new HashMap<Integer, SubordinateUser>();
    private Map<Integer, Task> uncheckedTasksList = new HashMap<Integer, Task>(); // requests from sub.

    public ManagerUser(String name) {
        super(name);
        // subordinateList = new HashMap<Integer, SubordinateUser>();
        // uncheckedTasksList = new HashMap<Integer, Task>();
    }

    public Map<Integer, SubordinateUser> getSubordinateList() {
        return subordinateList;
    }

    public Map<Integer, Task> getUncheckedTasksList() {
        return uncheckedTasksList;
    }
}
