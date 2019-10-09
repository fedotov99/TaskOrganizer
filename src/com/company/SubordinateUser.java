package com.company;

enum PositionType {
    Junior,
    Middle,
    Senior
}

public class SubordinateUser extends User {
    private ManagerUser manager;
    private int score = 0;
    private PositionType position;

    public SubordinateUser(String name, ManagerUser manager, int score, PositionType position) {
        super(name);
        this.manager = manager;
        this.score = score;
        this.position = position;
        // manager.addSubordinate(this);
    }

    @Override
    public void completeTask (int id, String report) {            // implements abstract method in User
        if (localUserTaskList.get(id) != null) {
            localUserTaskList.get(id).setReport(report);
            localUserTaskList.get(id).setCompleted(true);
            sendRequestForTaskApprovalToManager(localUserTaskList.get(id));
            localUserTaskList.remove(id);
        }
    }

    private void sendRequestForTaskApprovalToManager(Task task) {
        this.manager.addToUncheckedTasksList(task);
    }
}
