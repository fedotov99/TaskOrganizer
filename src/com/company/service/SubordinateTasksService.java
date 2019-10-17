package com.company.service;

import com.company.model.*;

public class SubordinateTasksService extends UserTasksService {
    public SubordinateUser createSubordinateUser(String name, ManagerUser manager, int score, PositionType position) {
        SubordinateUser newSU = new SubordinateUser(name, manager, score, position);
        ManagerTasksService.addSubordinateToManager(manager, newSU);
        return newSU;
    }

    @Override
    public void completeTask (User subordinate, int id, String report) {            // implements abstract method in User
        if (subordinate instanceof SubordinateUser) {
            if (subordinate.getLocalUserTaskList().get(id) != null) {
                subordinate.getLocalUserTaskList().get(id).setReport(report);
                subordinate.getLocalUserTaskList().get(id).setCompleted(true);
                sendRequestForTaskApprovalToManager((SubordinateUser)subordinate, subordinate.getLocalUserTaskList().get(id));
                subordinate.getLocalUserTaskList().remove(id);
            }
        } else {
            System.out.println("Wrong user!");
        }
    }

    protected void sendRequestForTaskApprovalToManager(SubordinateUser subordinate, Task task) {
        ManagerTasksService.addToUncheckedTasksListOfManager(subordinate.getManager(), task);
    }

}
