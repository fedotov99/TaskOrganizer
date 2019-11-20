package com.company.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class SubordinateUser extends User {
    // Found cycle for field 'subordinateList' in type 'ManagerUser' for path 'manager -> subordinateList -> manager'
    // private ManagerUser manager;
    private String managerID;
    private int score = 0;
    private PositionType position;

    public SubordinateUser() {
    }

    public SubordinateUser(String name, String managerID, int score, PositionType position) {
        super(name);
        this.managerID = managerID;
        this.score = score;
        this.position = position;
    }

    public String getManagerID() {
        return managerID;
    }

    public void setManagerID(String managerID) {
        this.managerID = managerID;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public PositionType getPosition() {
        return position;
    }

    public void setPosition(PositionType position) {
        this.position = position;
    }
}
