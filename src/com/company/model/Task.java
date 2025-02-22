package com.company.model;

public class Task {
    private int taskID;
    private static int counter = 1;
    private String description;
    private String report = ""; // special public method of User's derived classes will set report (when complete task)
    private boolean completed = false;
    private PriorityType priority;
    private User executor = null;

    public Task(String description) {
        this.taskID = counter++;
        this.description = description;
        this.priority = PriorityType.NORMAL;
    }

    public Task(String description, PriorityType priority) {
        this.taskID = counter++;
        this.description = description;
        this.report = "";
        this.completed = false;
        this.priority = priority;
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

    public User getExecutor() {
        return executor;
    }

    public void setExecutor(User executor) {
        this.executor = executor;
    }

    @Override
    public String toString() {
        return this.getTaskID() + " " + this.getDescription() + " " + this.getPriority() + " " +
                this.isCompleted() + "\n";
    }
}
