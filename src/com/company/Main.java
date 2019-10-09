package com.company;

public class Main {

    public static void main(String[] args) {
        Task t  = new Task("Write email to customer");
        t.setPriority(PriorityType.URGENT);
        t.printTaskInfo(); // 1 Write email to customer URGENT false

        ManagerUser Ann = new ManagerUser("Ann");
        SubordinateUser Paul = new SubordinateUser("Paul", Ann, 0, PositionType.Junior);
        System.out.println(Ann.getUserID());
        System.out.println(Paul.getUserID());
        System.out.println(Ann.getName());
        System.out.println(Paul.getName());
        Ann.addTask(t); // bug
        Ann.assignTaskToSubordinate(t, Paul);
        Paul.completeTask(t.getTaskID(), "I made this task, Ann, check it please!");
    }
}