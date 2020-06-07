package com.company.model.notification;

public class UncheckedTaskNotificationMessage {
    private String taskID;
    private String managerID;

    public UncheckedTaskNotificationMessage() {
    }

    public UncheckedTaskNotificationMessage(String taskID, String managerID) {
        this.taskID = taskID;
        this.managerID = managerID;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getManagerID() {
        return managerID;
    }

    public void setManagerID(String managerID) {
        this.managerID = managerID;
    }
}
