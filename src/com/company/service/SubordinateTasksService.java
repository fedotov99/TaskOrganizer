package com.company;

public class SubordinateTasksService extends UserTasksService {
    @Override
    public void completeTask (User subordinate, int id, String report) {            // implements abstract method in User
        if (subordinate instanceof SubordinateUser) {
            if (subordinate.localUserTaskList.get(id) != null) {
                subordinate.localUserTaskList.get(id).setReport(report);
                subordinate.localUserTaskList.get(id).setCompleted(true);
                sendRequestForTaskApprovalToManager((SubordinateUser)subordinate, subordinate.localUserTaskList.get(id));
                subordinate.localUserTaskList.remove(id);
            }
        } else {
            System.out.println("Wrong user!");
        }
    }

    protected void sendRequestForTaskApprovalToManager(SubordinateUser subordinate, Task task) {
        ManagerTasksService.addToUncheckedTasksListOfManager(subordinate.manager, task);
    }

}
