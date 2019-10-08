package com.company;
import java.util.Date;

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
    private boolean completed;
    private PriorityType priority;

    Task(String descr) {
        taskID = counter++;
        description = descr;
        completed = false;
        priority = PriorityType.NORMAL;
    }

    Task(String descr, PriorityType prior) {
        taskID = counter++;
        description = descr;
        completed = false;
        priority = prior;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
