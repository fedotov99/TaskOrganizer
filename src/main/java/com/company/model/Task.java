package com.company.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Task {
    @Id
    private String taskID;
    private String description;
    private String report = ""; // special public method of User's derived classes will set report (when complete task)
    private boolean completed = false;
    private PriorityType priority;
    // Found cycle for field 'executor' in type 'Task' for path 'localUserTaskList -> executor -> localUserTaskList'
    // private User executor = null;
    private String executorID;

    public Task() {
    }

    public Task(String description) {
        this.description = description;
        this.priority = PriorityType.NORMAL;
    }

    public Task(String description, PriorityType priority) {
        this.description = description;
        this.report = "";
        this.completed = false;
        this.priority = priority;
    }

    public String getTaskID() {
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

    public String getExecutorID() {
        return executorID;
    }

    public void setExecutorID(String executorID) {
        this.executorID = executorID;
    }

    @Override
    public String toString() {
        return this.getTaskID() + " " + this.getDescription() + " " + this.getPriority() + " " +
                this.isCompleted() + "\n";
    }
}
