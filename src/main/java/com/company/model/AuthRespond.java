package com.company.model;

public class AuthRespond {
    String sessionUserID;
    String role;

    public AuthRespond(String sessionUserID, String role) {
        this.sessionUserID = sessionUserID;
        this.role = role;
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
}
