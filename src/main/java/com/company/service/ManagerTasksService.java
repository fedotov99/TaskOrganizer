package com.company.service;

import com.company.model.*;
import com.company.repository.ManagerUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.HashMap;

@Service
public class ManagerTasksService extends UserTasksService {
    @Autowired
    private ManagerUserRepository managerUserRepository;
    @Autowired
    private TaskService taskService;
    @Autowired
    private SubordinateTasksService subordinateTasksService;

    public ManagerUser createManagerUser(String name) {
        return managerUserRepository.save(new ManagerUser(name));
    }

    public ManagerUser getByUserID(String id) {
        return managerUserRepository.findByUserID(id);
    }

    public List<ManagerUser> getByName(String name) {
        return managerUserRepository.findByName(name);
    }

    public List<ManagerUser> getAll(){
        return managerUserRepository.findAll();
    }

    public ManagerUser updateManagerUser(String id, String name, Map<String, SubordinateUser> subordinateList, Map<String, Task> uncheckedTasksList) {
        ManagerUser newMU = managerUserRepository.findByUserID(id);
        newMU.setName(name);
        newMU.setSubordinateList(subordinateList);
        newMU.setUncheckedTasksList(uncheckedTasksList);
        return managerUserRepository.save(newMU);
    }

    public ManagerUser updateManagerUserTaskList(String id, Map<String, Task> localUserTaskList) {
        ManagerUser newMU = managerUserRepository.findByUserID(id);
        newMU.setLocalUserTaskList(localUserTaskList);
        return managerUserRepository.save(newMU);
    }

    public ManagerUser updateManagerUserSubordinateList(String id, Map<String, SubordinateUser>  subordinateList) {
        ManagerUser newMU = managerUserRepository.findByUserID(id);
        newMU.setSubordinateList(subordinateList);
        return managerUserRepository.save(newMU);
    }

    public ManagerUser updateManagerUserUncheckedTaskList(String id, Map<String, Task> uncheckedTasksList) {
        ManagerUser newMU = managerUserRepository.findByUserID(id);
        newMU.setUncheckedTasksList(uncheckedTasksList);
        return managerUserRepository.save(newMU);
    }

    public void deleteById(String id) {
        managerUserRepository.deleteById(id);
    }

    public void deleteAll() {
        managerUserRepository.deleteAll();
    }

    private static final Map<PriorityType, Integer> taskValue = new HashMap<PriorityType, Integer>();
    {
        taskValue.put(PriorityType.URGENT, 10);
        taskValue.put(PriorityType.HIGH, 7);
        taskValue.put(PriorityType.NORMAL, 5);
        taskValue.put(PriorityType.LOW, 3);
    }

    @Override
    public void completeTask (User manager, String taskID, String report) {  // implements abstract method in User
        if (manager instanceof ManagerUser) {
            if (manager.getLocalUserTaskList().get(taskID) != null) {
                manager.getLocalUserTaskList().get(taskID).setReport(report);
                manager.getLocalUserTaskList().get(taskID).setCompleted(true);
                manager.getLocalUserTaskList().remove(taskID);

                // update DB
                Task nT = taskService.getByTaskID(taskID);
                nT = taskService.updateTaskReportAndCompleted(taskID, report,true);
                ManagerUser newMU = getByUserID(manager.getUserID());
                newMU = updateManagerUserTaskList(manager.getUserID(), manager.getLocalUserTaskList());
            }
        } else {
            System.out.println("Wrong user!");
        }
    }

    @Override
    public void deleteTaskFromLocalUserTaskList(User manager, String taskID) {
        if (manager instanceof ManagerUser) {
            if (manager.getLocalUserTaskList().containsKey(taskID)) {
                manager.getLocalUserTaskList().remove(taskID);

                // update DB
                ManagerUser newMU = getByUserID(manager.getUserID());
                newMU = updateManagerUserTaskList(manager.getUserID(), manager.getLocalUserTaskList());
            }
        } else {
            System.out.println("Wrong user!");
        }
    }

    public void addSubordinateToManager(ManagerUser manager, SubordinateUser su) { // suppose that subordinate knows about his manager
        manager.getSubordinateList().putIfAbsent(su.getUserID(), su);

        // update DB
        ManagerUser newMU = getByUserID(manager.getUserID());
        newMU = updateManagerUserSubordinateList(manager.getUserID(), manager.getSubordinateList());
    }

    public void addToUncheckedTasksListOfManager(ManagerUser manager, Task task) {  // this method will be used by any subordinate who wants to send task request
        manager.getUncheckedTasksList().putIfAbsent(task.getTaskID(), task);

        // update DB
        ManagerUser newMU = getByUserID(manager.getUserID());
        newMU = updateManagerUserUncheckedTaskList(manager.getUserID(), manager.getUncheckedTasksList());
    }

    public void approveTaskInUncheckedTasksListOfManager(ManagerUser manager, Task task) {
        if (manager.getUncheckedTasksList().containsKey(task.getTaskID())) {
            SubordinateUser subordinateUser = subordinateTasksService.getByUserID(task.getExecutorID());
            subordinateUser.setScore(subordinateUser.getScore() + taskValue.get(task.getPriority()));

            // update DB
            subordinateUser = subordinateTasksService.updateSubordinateUserScore(subordinateUser.getUserID(), subordinateUser.getScore());

            manager.getUncheckedTasksList().remove(task.getTaskID());

            // update DB
            ManagerUser newMU = getByUserID(manager.getUserID());
            newMU = updateManagerUserUncheckedTaskList(manager.getUserID(), manager.getUncheckedTasksList());
        }
    }

    public void declineTaskInUncheckedTasksListOfManager(ManagerUser manager, Task task) { // changes requested
        if (manager.getUncheckedTasksList().containsKey(task.getTaskID())) {
            SubordinateUser subordinateUser = subordinateTasksService.getByUserID(task.getExecutorID());
            assignTaskToSubordinateOfManager(manager, task, subordinateUser); // send back
            // concerning DB update, see assignTaskToSubordinateOfManager() method

            manager.getUncheckedTasksList().remove(task.getTaskID());

            // update DB
            ManagerUser newMU = getByUserID(manager.getUserID());
            newMU = updateManagerUserUncheckedTaskList(manager.getUserID(), manager.getUncheckedTasksList());
        }
    }

    public void addTaskToUser(User user, Task task) {  // both to manager or subordinate. this method can be moved to AdminService e.g.
        task.setCompleted(false);
        task.setExecutorID(user.getUserID());
        user.getLocalUserTaskList().put(task.getTaskID(), task);

        // update DB
        Task nT = taskService.getByTaskID(task.getTaskID());
        nT = taskService.updateTaskCompletedAndExecutor(task.getTaskID(), false, user.getUserID());
    }

    public void assignTaskToSubordinateOfManager(ManagerUser manager, Task task, SubordinateUser su) {
        if ((manager.getLocalUserTaskList().containsKey(task.getTaskID()) || manager.getUncheckedTasksList().containsKey(task.getTaskID())) && manager.getSubordinateList().containsKey(su.getUserID())) { // can assign only OUR employee
            addTaskToUser(su, task);
            // concerning DB update, see addTaskToUser() method
        }
        deleteTaskFromLocalUserTaskList(manager, task.getTaskID()); // sent to subordinate and got rid of this task
        // concerning DB update, see deleteTaskFromLocalUserTaskList() method
    }

    public int getSubordinatesSizeOfManager(ManagerUser manager) {
        return manager.getSubordinateList().size();
    }

    public int getUncheckedTasksListSize(ManagerUser manager) {
        return manager.getUncheckedTasksList().size();
    }

    public SubordinateUser[] selectSubordinatesWithDefiniteTaskType(ManagerUser manager, Predicate<Task> taskPredicate) {
        Predicate<SubordinateUser> subordinateUserPredicate = new Predicate<SubordinateUser>() {
            @Override
            public boolean test(SubordinateUser subordinateUser) {
                return subordinateUser.getLocalUserTaskList().values().stream().anyMatch(taskPredicate);
            }
        };
        Map<String, SubordinateUser> subList = manager.getSubordinateList();
        return subList.values().stream().filter(subordinateUserPredicate).toArray(SubordinateUser[]::new);
    }
}
