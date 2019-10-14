package com.company;

public class ManagerTasksService extends UserTasksService {
    @Override
    public void completeTask (User manager, int id, String report) {            // implements abstract method in User
        if (manager instanceof ManagerUser) {
            if (manager.localUserTaskList.get(id) != null) {
                manager.localUserTaskList.get(id).setReport(report);
                manager.localUserTaskList.get(id).setCompleted(true);
                manager.localUserTaskList.remove(id);
            }
        } else {
            System.out.println("Wrong user!");
        }
    }

    public void addSubordinateToManager(ManagerUser manager, SubordinateUser su) {
        manager.subordinateList.putIfAbsent(su.getUserID(), su);
    }

    public static void addToUncheckedTasksListOfManager(ManagerUser manager, Task task) {  // this method will be used by any subordinate who wants to send task request
        manager.uncheckedTasksList.putIfAbsent(task.getTaskID(), task);
    }

    public void assignTaskToSubordinateOfManager(ManagerUser manager, Task task, SubordinateUser su) {
        if (manager.localUserTaskList.containsKey(task) && manager.subordinateList.containsKey(su)) { // can assign only OUR employee
            addTaskToUser(su, task);
        }
    }
}
