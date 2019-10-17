package com.company.service;

import com.company.model.*;

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

    protected static void addSubordinateToManager(ManagerUser manager, SubordinateUser su) {
        manager.getSubordinateList().putIfAbsent(su.getUserID(), su);
    }

    protected static void addToUncheckedTasksListOfManager(ManagerUser manager, Task task) {  // this method will be used by any subordinate who wants to send task request
        manager.getUncheckedTasksList().putIfAbsent(task.getTaskID(), task);
    }

    public void approveTaskInUncheckedTasksListOfManager(ManagerUser manager, Task task) {
        if (manager.getUncheckedTasksList().containsKey(task.getTaskID())) {
            manager.getUncheckedTasksList().remove(task.getTaskID());

            if (task.getExecutor() instanceof SubordinateUser) {
                int currentScore = ((SubordinateUser) task.getExecutor()).getScore();
                System.out.println("priority " + task.getPriority());
                switch (task.getPriority()) {
                    case URGENT:
                        ((SubordinateUser) task.getExecutor()).setScore(currentScore += 10);
                        break;
                    case HIGH:
                        ((SubordinateUser) task.getExecutor()).setScore(currentScore += 7);
                        break;
                    case NORMAL:
                        ((SubordinateUser) task.getExecutor()).setScore(currentScore += 5);
                        break;
                    default:
                        ((SubordinateUser) task.getExecutor()).setScore(currentScore += 3);
                        break;
                }
            }
        }
    }

    public void assignTaskToSubordinateOfManager(ManagerUser manager, Task task, SubordinateUser su) {
        if (manager.getLocalUserTaskList().containsKey(task.getTaskID()) && manager.getSubordinateList().containsKey(su.getUserID())) { // can assign only OUR employee
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
