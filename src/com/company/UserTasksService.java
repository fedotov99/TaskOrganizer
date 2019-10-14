package com.company;

public abstract class UserTasksService {
    public void addTaskToUser(User user, Task task) {  // bug
        Integer i = task.getTaskID();
        user.localUserTaskList.put(i, task);
        //localUserTaskList.putIfAbsent(i, task);
    }

    public abstract void completeTask (User user, int id, String report);

    public void deleteTask(User user, int id) {
        if (user.localUserTaskList.containsKey(id)) {
            user.localUserTaskList.remove(id);
        }
    }
}