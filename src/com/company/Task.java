package com.company;
import java.util.Date;
import java.util.Map;

enum PriorityType {
    LOW,
    NORMAL,
    HIGH,
    URGENT
}

public class Task {
    private int taskID;
    private static int counter = 1;
    private String description;
    private String report; // special public method of User's derived classes will set report (when complete task)
    private boolean completed;
    private PriorityType priority;

    Task(String _description) {
        taskID = counter++;
        description = _description;
        report = "";
        completed = false;
        priority = PriorityType.NORMAL;
    }

    Task(String _description, PriorityType _priority) {
        taskID = counter++;
        description = _description;
        report = "";
        completed = false;
        priority = _priority;
    }

    public int getTaskID() {
        return taskID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public PriorityType getPriority() {
        return priority;
    }

    public void setPriority(PriorityType priority) {
        this.priority = priority;
    }

    public void printTaskInfo() {
        System.out.print(this.getTaskID() + " " + this.getDescription() + " " + this.getPriority() + " " +
                this.isCompleted() + "\n");
    }
}
