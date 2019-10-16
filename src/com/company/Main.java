package com.company;

import com.company.model.*;
import com.company.service.ManagerTasksService;
import com.company.service.SubordinateTasksService;

public class Main {

    public static void main(String[] args) {
        ManagerTasksService managerService = new ManagerTasksService();
        SubordinateTasksService subService = new SubordinateTasksService();

        Task t  = new Task("Write email to customer");
        t.setPriority(PriorityType.URGENT);
        System.out.println(t.toString()); // 1 Write email to customer URGENT false

        ManagerUser Ann = new ManagerUser("Ann");
        SubordinateUser Paul = new SubordinateUser("Paul", Ann, 0, PositionType.Junior);

        managerService.addTaskToUser(Ann, t);
        managerService.assignTaskToSubordinateOfManager(Ann, t, Paul);
        subService.completeTask(Paul, t.getTaskID(), "I made this task, Ann, check it please!");
    }
}