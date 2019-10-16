package com.company;

import com.company.model.*;
import com.company.service.ManagerTasksService;
import com.company.service.SubordinateTasksService;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.*;
import java.util.function.*;

public class Main {

    public static void main(String[] args) {
        ManagerTasksService managerService = new ManagerTasksService();
        SubordinateTasksService subService = new SubordinateTasksService();

        Task t  = new Task("Write email to customer");
        t.setPriority(PriorityType.URGENT);
        System.out.println(t.toString()); // 1 Write email to customer URGENT false

        // create subordinates and link them with their manager
        ManagerUser Ann = new ManagerUser("Ann");
        SubordinateUser Paul = new SubordinateUser("Paul", Ann, 0, PositionType.Junior);
        managerService.addSubordinateToManager(Ann, Paul);
        SubordinateUser Mike = new SubordinateUser("Mike", Ann, 0, PositionType.Middle);
        managerService.addSubordinateToManager(Ann, Mike);
        SubordinateUser Jack = new SubordinateUser("Jack", Ann, 0, PositionType.Junior);
        managerService.addSubordinateToManager(Ann, Jack);
        SubordinateUser John = new SubordinateUser("John", Ann, 0, PositionType.Middle);
        managerService.addSubordinateToManager(Ann, John);
        SubordinateUser Kate = new SubordinateUser("Kate", Ann, 0, PositionType.Junior);
        managerService.addSubordinateToManager(Ann, Jack);

        managerService.addTaskToUser(Ann, t);
        managerService.assignTaskToSubordinateOfManager(Ann, t, Paul);
        subService.completeTask(Paul, t.getTaskID(), "I made this task, Ann, check it please!");  // now no subordinates has tasks

        Task t1  = new Task("Write interface", PriorityType.NORMAL);
        Task t2  = new Task("Make UML model", PriorityType.URGENT);
        Task t3  = new Task("Buy paper for printer", PriorityType.LOW);
        Task t4  = new Task("Call customer", PriorityType.URGENT);
        Task t5  = new Task("Buy coffee", PriorityType.LOW);
        Task t6  = new Task("Prepare for a conference", PriorityType.URGENT);
        Task t7  = new Task("Install software in the 3rd department", PriorityType.NORMAL);

        subService.addTaskToUser(Paul, t1);
        subService.addTaskToUser(Mike, t2);  // now Mike has urgent task
        subService.addTaskToUser(Jack, t3);
        subService.addTaskToUser(John, t4);  // now John has urgent task
        subService.addTaskToUser(Kate, t5);
        subService.addTaskToUser(Jack, t6);  // now Jack has urgent task too
        subService.addTaskToUser(Jack, t7);  // now Jack has two urgent tasks

        // print subordinates of Ann who has urgent tasks
        Map<Integer, SubordinateUser> subList = Ann.getSubordinateList();
        Stream<SubordinateUser> subStream = subList.values().stream();

        Predicate<Task> isTaskUrgent = task -> task.getPriority() == PriorityType.URGENT;
        Predicate<SubordinateUser> hasUrgentTask = new Predicate<SubordinateUser>() {
            @Override
            public boolean test(SubordinateUser subordinateUser) {
                Stream<Task> taskStream = subordinateUser.getLocalUserTaskList().values().stream();
                return taskStream.anyMatch(isTaskUrgent);
            }
        };

        subStream
                .filter(hasUrgentTask)
                .forEach(i->System.out.println(i.toString()));
    }
}