package com.company.service;

import com.company.model.*;
import com.company.repository.ManagerUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;

@Service
public class ManagerTasksService extends UserTasksService {
    @Autowired
    private ManagerUserRepository managerUserRepository;
    @Autowired
    private TaskService taskService;
    @Autowired
    private SubordinateTasksService subordinateTasksService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public ManagerUser createManagerUser(String name, String email, String password) {
        return managerUserRepository.save(new ManagerUser(name, email, passwordEncoder.encode(password)));
    }

    public ManagerUser getByUserID(String id) {
        return managerUserRepository.findByUserID(id);
    }

    public List<ManagerUser> getByName(String name) {
        return managerUserRepository.findByName(name);
    }

    public ManagerUser getByEmail(String email) {
        return managerUserRepository.findByEmail(email);
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
    public void updateTaskInLocalUserTaskList(User user, String taskID) {
        if (user instanceof ManagerUser) {
            if (user.getLocalUserTaskList().containsKey(taskID)) {
                user.getLocalUserTaskList().remove(taskID);
                Task t = taskService.getByTaskID(taskID);
                user.getLocalUserTaskList().put(t.getTaskID(), t);

                // update DB
                ManagerUser newMU = getByUserID(user.getUserID());
                newMU = updateManagerUserTaskList(user.getUserID(), user.getLocalUserTaskList());
            }
        }
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
        }
    }

    public void deleteTaskFromUncheckedTaskList(ManagerUser manager, String taskID) {
        if (manager.getUncheckedTasksList().containsKey(taskID)) {
            manager.getUncheckedTasksList().remove(taskID);

            // update DB
            ManagerUser newMU = getByUserID(manager.getUserID());
            newMU = updateManagerUserUncheckedTaskList(manager.getUserID(), manager.getUncheckedTasksList());
        }
    }

    public void addSubordinateToManager(ManagerUser manager, SubordinateUser su) { // suppose that subordinate knows about his manager
        manager.getSubordinateList().putIfAbsent(su.getUserID(), su);

        // update DB
        ManagerUser newMU = getByUserID(manager.getUserID());
        newMU = updateManagerUserSubordinateList(manager.getUserID(), manager.getSubordinateList());
    }

    public List<Task> getManagerTaskList(String managerID) {
        ManagerUser manager = getByUserID(managerID);
        Map<String, Task> managerTasksList = manager.getLocalUserTaskList();
        return new LinkedList<>(managerTasksList.values());
    }

    public List<SubordinateUser> getSubordinateList(String managerID) {
        ManagerUser manager = getByUserID(managerID);
        Map<String, SubordinateUser> subordinateList = manager.getSubordinateList();
        return new LinkedList<>(subordinateList.values());
    }

    public List<Task> getUncheckedTaskList(String managerID) {
        ManagerUser manager = getByUserID(managerID);
        Map<String, Task> uncheckedTasksList = manager.getUncheckedTasksList();
        return new LinkedList<>(uncheckedTasksList.values());
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
            // in order to sync subordinate's score, update it in object
            updateSubordinateScoreInSubordinateList(manager.getUserID(), subordinateUser.getUserID());

            manager.getUncheckedTasksList().remove(task.getTaskID());

            // update DB
            ManagerUser newMU = getByUserID(manager.getUserID());
            newMU = updateManagerUserUncheckedTaskList(manager.getUserID(), manager.getUncheckedTasksList());
        }
    }

    public void updateSubordinateScoreInSubordinateList(String managerID, String subordinateID) {
        ManagerUser manager = getByUserID(managerID);
        SubordinateUser subordinate = subordinateTasksService.getByUserID(subordinateID);
        manager.getSubordinateList().get(subordinateID).setScore(subordinate.getScore());
        manager = updateManagerUserSubordinateList(managerID, manager.getSubordinateList());
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
        if (user instanceof ManagerUser)
            user = updateManagerUserTaskList(user.getUserID(), user.getLocalUserTaskList());
        else if (user instanceof SubordinateUser)
            user = subordinateTasksService.updateSubordinateUserTaskList(user.getUserID(), user.getLocalUserTaskList());
        task = taskService.updateTaskCompletedAndExecutor(task.getTaskID(), false, user.getUserID());
    }

    public void assignTaskToSubordinateOfManager(ManagerUser manager, Task task, SubordinateUser su) {
        if ((manager.getLocalUserTaskList().containsKey(task.getTaskID()) || manager.getUncheckedTasksList().containsKey(task.getTaskID())) && manager.getSubordinateList().containsKey(su.getUserID())) { // can assign only OUR employee
            addTaskToUser(su, task);
            // concerning DB update, see addTaskToUser() method
        }
        deleteTaskFromLocalUserTaskList(manager, task.getTaskID()); // sent to subordinate and got rid of this task
        deleteTaskFromUncheckedTaskList(manager, task.getTaskID()); // when manager wants to assign task to another subordinate after review
        // concerning DB update, see deleteTaskFromLocalUserTaskList() method
    }

    public int getSubordinatesSizeOfManager(ManagerUser manager) {
        return manager.getSubordinateList().size();
    }

    public int getUncheckedTasksListSize(ManagerUser manager) {
        return manager.getUncheckedTasksList().size();
    }

    public List<SubordinateUser> getSubordinatesWithUrgentTasks(String managerID) {
        ManagerUser manager = getByUserID(managerID);
        Predicate<Task> isTaskUrgent = task -> task.getPriority() == PriorityType.URGENT;
        List<SubordinateUser> returnList = new LinkedList<>(Arrays.asList(selectSubordinatesWithDefiniteTaskType(manager, isTaskUrgent)));
        return returnList;
    }

    public SubordinateUser[] selectSubordinatesWithDefiniteTaskType(ManagerUser manager, Predicate<Task> taskPredicate) {
        Predicate<SubordinateUser> subordinateUserPredicate = new Predicate<SubordinateUser>() {
            @Override
            public boolean test(SubordinateUser subordinateUser) {
                // we have to take sub from DB, because reference to sub's local task list into manager's sub list is not being synced with real sub's task list.
                SubordinateUser subordinateFromDB = subordinateTasksService.getByUserID(subordinateUser.getUserID());
                return subordinateFromDB.getLocalUserTaskList().values().stream().anyMatch(taskPredicate);
            }
        };
        Map<String, SubordinateUser> subList = manager.getSubordinateList();
        SubordinateUser[] returnArray = subList.values().stream().filter(subordinateUserPredicate).toArray(SubordinateUser[]::new);
        return returnArray;
    }
}
