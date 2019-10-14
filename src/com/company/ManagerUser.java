package com.company;

import java.util.HashMap;
import java.util.Map;

public class ManagerUser extends User {
    // each manager will have one or more sub.
    protected Map<Integer, SubordinateUser>  subordinateList = new HashMap<Integer, SubordinateUser>();
    protected Map<Integer, Task> uncheckedTasksList = new HashMap<Integer, Task>(); // requests from sub.

    ManagerUser(String _name) {
        super(_name);
        // subordinateList = new HashMap<Integer, SubordinateUser>();
        // uncheckedTasksList = new HashMap<Integer, Task>();
    }

    public int getSubordinatesSize() {
        return subordinateList.size();
    }

    public int getUncheckedTasksListSize() {
        return uncheckedTasksList.size();
    }
}
