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

    public SubordinateUser(String _name, ManagerUser _manager, int _score, PositionType _position) {
        super(_name);
        manager = _manager;
        score = _score;
        position = _position;
        // manager.addSubordinate(this);
    }

    @Override
    public void completeTask (int _id, String _report) {            // implements abstract method in User
        if (localUserTaskList.get(_id) != null) {
            localUserTaskList.get(_id).setReport(_report);
            localUserTaskList.get(_id).setCompleted(true);
            sendRequestForTaskApprovalToManager(localUserTaskList.get(_id));
            localUserTaskList.remove(_id);
        }
    }

    private void sendRequestForTaskApprovalToManager(Task _task) {
        this.manager.addToUncheckedTasksList(_task);
    }
}
