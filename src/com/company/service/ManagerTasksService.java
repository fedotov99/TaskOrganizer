package com.company.service;

import com.company.model.ManagerUser;
import com.company.model.SubordinateUser;
import com.company.model.Task;
import com.company.model.User;

import java.util.Map;
import java.util.stream.*;
import java.util.function.*;

public class ManagerTasksService extends UserTasksService {
    @Override
    public void completeTask (User manager, int id, String report) {            // implements abstract method in User
        if (manager instanceof ManagerUser) {
            if (manager.getLocalUserTaskList().get(id) != null) {
                manager.getLocalUserTaskList().get(id).setReport(report);
                manager.getLocalUserTaskList().get(id).setCompleted(true);
                manager.getLocalUserTaskList().remove(id);
            }
        } else {
            System.out.println("Wrong user!");
        }
    }

    public static void addSubordinateToManager(ManagerUser manager, SubordinateUser su) {
        manager.getSubordinateList().putIfAbsent(su.getUserID(), su);
    }

    public static void addToUncheckedTasksListOfManager(ManagerUser manager, Task task) {  // this method will be used by any subordinate who wants to send task request
        manager.getUncheckedTasksList().putIfAbsent(task.getTaskID(), task);
    }

    public void assignTaskToSubordinateOfManager(ManagerUser manager, Task task, SubordinateUser su) {
        if (manager.getLocalUserTaskList().containsKey(task) && manager.getSubordinateList().containsKey(su)) { // can assign only OUR employee
            addTaskToUser(su, task);
        }
    }

    public int getSubordinatesSizeOfManager(ManagerUser manager) {
        return manager.getSubordinateList().size();
    }

    public int getUncheckedTasksListSize(ManagerUser manager) {
        return manager.getUncheckedTasksList().size();
    }

    public SubordinateUser[] selectSubordinatesWithDefiniteTaskType(ManagerUser manager, Predicate<Task> taskPredicate) {
        Predicate<SubordinateUser> subordinateUserPredicate = new Predicate<SubordinateUser>() {
            @Override
            public boolean test(SubordinateUser subordinateUser) {
                return subordinateUser.getLocalUserTaskList().values().stream().anyMatch(taskPredicate);
            }
        };
        Map<Integer, SubordinateUser> subList = manager.getSubordinateList();
        return subList.values().stream().filter(subordinateUserPredicate).toArray(SubordinateUser[]::new);
    }
}
