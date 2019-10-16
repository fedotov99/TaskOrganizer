package com.company;

public class SubordinateUser extends User {
    protected ManagerUser manager;
    private int score = 0;
    private PositionType position;

    public SubordinateUser(String name, ManagerUser manager, int score, PositionType position) {
        super(name);
        this.manager = manager;
        this.score = score;
        this.position = position;
        // manager.addSubordinate(this);
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
