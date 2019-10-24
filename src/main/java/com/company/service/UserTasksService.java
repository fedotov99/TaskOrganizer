package com.company.service;

import com.company.model.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public abstract class UserTasksService {

    public abstract void completeTask (User user, String taskID, String report);

    public abstract void deleteTaskFromLocalUserTaskList(User user, String taskID);
}