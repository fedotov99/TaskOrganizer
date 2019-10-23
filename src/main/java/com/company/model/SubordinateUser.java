package com.company.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class SubordinateUser extends User {
    private ManagerUser manager;
    private int score = 0;
    private PositionType position;

    public SubordinateUser(String name, ManagerUser manager, int score, PositionType position) {
        super(name);
        this.manager = manager;
        this.score = score;
        this.position = position;
        // how to call automatically for this created object ManagerTasksService::addSubordinateToManager() ?
    }

    public ManagerUser getManager() {
        return manager;
    }

    public void setManager(ManagerUser manager) {
        this.manager = manager;
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
