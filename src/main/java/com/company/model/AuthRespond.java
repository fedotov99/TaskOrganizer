package com.company.model;

public class AuthRespond {
    String sessionUserID;
    String role;
    boolean success;

    public AuthRespond() {
    }

    public AuthRespond(String sessionUserID, String role, boolean success) {
        this.sessionUserID = sessionUserID;
        this.role = role;
        this.success = success;
    }

    public String getSessionUserID() {
        return sessionUserID;
    }

    public void setSessionUserID(String sessionUserID) {
        this.sessionUserID = sessionUserID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
