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
    protected ManagerUserRepository managerUserRepository;

    public ManagerUser createManagerUser(String name) {
        return managerUserRepository.save(new ManagerUser(name));
    }

    public ManagerUser getByUserID(String id) {
        return managerUserRepository.findByUserID(id);
    }

    public ManagerUser getByName(String name) {
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
    public void completeTask (User manager, int id, String report) {            // implements abstract method in User
        if (manager instanceof ManagerUser) {
            if (manager.getLocalUserTaskList().get(id) != null) {
                manager.getLocalUserTaskList().get(id).setReport(report);
                manager.getLocalUserTaskList().get(id).setCompleted(true);
                manager.getLocalUserTaskList().remove(id);
            }
        } else {
            System.out.println("Wrong user!");
        }
    }

    protected static void addSubordinateToManager(ManagerUser manager, SubordinateUser su) {
        manager.getSubordinateList().putIfAbsent(su.getUserID(), su);
    }

    protected static void addToUncheckedTasksListOfManager(ManagerUser manager, Task task) {  // this method will be used by any subordinate who wants to send task request
        manager.getUncheckedTasksList().putIfAbsent(task.getTaskID(), task);
    }

    public void approveTaskInUncheckedTasksListOfManager(ManagerUser manager, Task task) {
        if (manager.getUncheckedTasksList().containsKey(task.getTaskID())) {
            if (task.getExecutor() instanceof SubordinateUser) {
                ((SubordinateUser) task.getExecutor()).setScore(((SubordinateUser) task.getExecutor()).getScore() + taskValue.get(task.getPriority()));
            }
            manager.getUncheckedTasksList().remove(task.getTaskID());
        }
    }

    public void declineTaskInUncheckedTasksListOfManager(ManagerUser manager, Task task) { // changes requested
        if (manager.getUncheckedTasksList().containsKey(task.getTaskID())) {
            if (task.getExecutor() instanceof SubordinateUser) {
                assignTaskToSubordinateOfManager(manager, task, (SubordinateUser)task.getExecutor()); // send back
            }
            manager.getUncheckedTasksList().remove(task.getTaskID());
        }
    }

    public void addTaskToUser(User user, Task task) {  // both to manager or subordinate. this method can be moved to AdminService e.g.
        String i = task.getTaskID();
        task.setCompleted(false);
        task.setExecutor(user);
        user.getLocalUserTaskList().put(i, task);
    }

    public void assignTaskToSubordinateOfManager(ManagerUser manager, Task task, SubordinateUser su) {
        if ((manager.getLocalUserTaskList().containsKey(task.getTaskID()) || manager.getUncheckedTasksList().containsKey(task.getTaskID())) && manager.getSubordinateList().containsKey(su.getUserID())) { // can assign only OUR employee
            addTaskToUser(su, task);
        }
        deleteTask(manager, task.getTaskID()); // sent to subordinate and got rid of this task
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
