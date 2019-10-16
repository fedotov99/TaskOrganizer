package com.company.service;

import com.company.model.Task;
import com.company.model.User;

public abstract class UserTasksService {
    public void addTaskToUser(User user, Task task) {  // bug
        Integer i = task.getTaskID();
        user.getLocalUserTaskList().put(i, task);
        //localUserTaskList.putIfAbsent(i, task);
    }

    public abstract void completeTask (User user, int id, String report);

    public void deleteTask(User user, int id) {
        if (user.getLocalUserTaskList().containsKey(id)) {
            user.getLocalUserTaskList().remove(id);
        }
    }
}