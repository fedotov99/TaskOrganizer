package com.company.model.notification;

public class TaskAssignedNotificationMessage {
    private String taskID;
    private String userID;

    public TaskAssignedNotificationMessage() {
    }

    public TaskAssignedNotificationMessage(String taskID, String userID) {
        this.taskID = taskID;
        this.userID = userID;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
