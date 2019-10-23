package com.company.service;

import com.company.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public abstract class UserTasksService {

    public abstract void completeTask (User user, int id, String report);

    public void deleteTask(User user, String id) {
        if (user.getLocalUserTaskList().containsKey(id)) {
            user.getLocalUserTaskList().remove(id);
        }
    }
}