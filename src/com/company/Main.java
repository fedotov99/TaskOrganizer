package com.company;

import com.company.model.*;
import com.company.service.ManagerTasksService;
import com.company.service.SubordinateTasksService;
import java.util.Arrays;
import java.util.function.Predicate;

public class Main {

    public static void main(String[] args) {
        ManagerTasksService managerService = new ManagerTasksService();
        SubordinateTasksService subService = new SubordinateTasksService();

        Task t = new Task("Write email to customer");
        t.setPriority(PriorityType.URGENT);
        System.out.println(t.toString()); // 1 Write email to customer URGENT false

        // create subordinates and link them with their manager
        ManagerUser Ann = new ManagerUser("Ann");
        SubordinateUser Paul = subService.createSubordinateUser("Paul", Ann, 0, PositionType.Junior);
        SubordinateUser Mike = subService.createSubordinateUser("Mike", Ann, 0, PositionType.Middle);
        SubordinateUser Jack = subService.createSubordinateUser("Jack", Ann, 0, PositionType.Junior);
        SubordinateUser John = subService.createSubordinateUser("John", Ann, 0, PositionType.Middle);
        SubordinateUser Kate = subService.createSubordinateUser("Kate", Ann, 0, PositionType.Junior);

        managerService.addTaskToUser(Ann, t);
        managerService.assignTaskToSubordinateOfManager(Ann, t, Paul);
        subService.completeTask(Paul, t.getTaskID(), "I made this task, Ann, check it please!");  // now no subordinates has tasks
        managerService.approveTaskInUncheckedTasksListOfManager(Ann, t);
        // managerService.declineTaskInUncheckedTasksListOfManager(Ann, t);
        System.out.println("Paul's score is " + Paul.getScore());  // Paul received +10 to score, because task was URGENT

        Task t1 = new Task("Write interface", PriorityType.NORMAL);
        Task t2 = new Task("Make UML model", PriorityType.URGENT);
        Task t3 = new Task("Buy paper for printer", PriorityType.LOW);
        Task t4 = new Task("Call customer", PriorityType.URGENT);
        Task t5 = new Task("Buy coffee", PriorityType.LOW);
        Task t6 = new Task("Prepare for a conference", PriorityType.URGENT);
        Task t7 = new Task("Install software in the 3rd department", PriorityType.NORMAL);

        subService.addTaskToUser(Paul, t1);
        subService.addTaskToUser(Mike, t2);  // now Mike has urgent task
        subService.addTaskToUser(Jack, t3);
        subService.addTaskToUser(John, t4);  // now John has urgent task
        subService.addTaskToUser(Kate, t5);
        subService.addTaskToUser(Jack, t6);  // now Jack has urgent task too
        subService.addTaskToUser(Jack, t7);  // now Jack has two urgent tasks

        System.out.println("\nSubordinates of Ann who has URGENT priority tasks: ");
        Predicate<Task> isTaskUrgent = task -> task.getPriority() == PriorityType.URGENT;
        SubordinateUser[] sUserArray = managerService.selectSubordinatesWithDefiniteTaskType(Ann, isTaskUrgent);
        Arrays.stream(sUserArray).forEach(i->System.out.println(i.toString()));
    }
}