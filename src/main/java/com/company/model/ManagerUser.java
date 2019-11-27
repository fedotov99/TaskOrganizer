package com.company.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Document
public class ManagerUser extends User {
    // each manager will have one or more sub.
    private Map<String, SubordinateUser>  subordinateList = new HashMap<String, SubordinateUser>();
    private Map<String, Task> uncheckedTasksList = new HashMap<String, Task>(); // requests from sub.

    public ManagerUser() {
    }

    public ManagerUser(String name) {
        super(name);
        // subordinateList = new HashMap<Integer, SubordinateUser>();
        // uncheckedTasksList = new HashMap<Integer, Task>();
    }

    public ManagerUser(String name, String email, String password) {
        super(name, email, password);
    }

    public Map<String, SubordinateUser> getSubordinateList() {
        return subordinateList;
    }

    public Map<String, Task> getUncheckedTasksList() {
        return uncheckedTasksList;
    }

    public void setSubordinateList(Map<String, SubordinateUser> subordinateList) {
        this.subordinateList = subordinateList;
    }

    public void setUncheckedTasksList(Map<String, Task> uncheckedTasksList) {
        this.uncheckedTasksList = uncheckedTasksList;
    }

    @Override
    public String toString() {
        return "Manager id " + getUserID() + " name " + getName();
    }
}
