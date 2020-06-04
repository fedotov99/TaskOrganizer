package com.company.service;

import com.company.model.*;
import com.company.repository.ManagerUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    public Mono<ManagerUser> createManagerUser(String name, String email, String password) {
        return managerUserRepository.save(new ManagerUser(name, email, passwordEncoder.encode(password)));
    }

    public Mono<ManagerUser> getByUserID(String id) {
        return managerUserRepository.findByUserID(id);
    }

    public Flux<ManagerUser> getByName(String name) {
        return managerUserRepository.findByName(name);
    }

    public Mono<ManagerUser> getByEmail(String email) {
        return managerUserRepository.findByEmail(email);
    }

    public Flux<ManagerUser> getAll(){
        return managerUserRepository.findAll();
    }

    public Mono<ManagerUser> updateManagerUser(String id, String name, Map<String, SubordinateUser> subordinateList, Map<String, Task> uncheckedTasksList) {
        return managerUserRepository.findByUserID(id)
                .flatMap(newMU -> {
                    newMU.setName(name);
                    newMU.setSubordinateList(subordinateList);
                    newMU.setUncheckedTasksList(uncheckedTasksList);

                    return managerUserRepository.save(newMU);
                });
    }

    public Mono<ManagerUser> updateManagerUserTaskList(String id, Map<String, Task> localUserTaskList) {
        return managerUserRepository.findByUserID(id)
                .flatMap(newMU -> {
                    newMU.setLocalUserTaskList(localUserTaskList);
                    return managerUserRepository.save(newMU);
                });
    }

    public Mono<ManagerUser> updateManagerUserSubordinateList(String id, Map<String, SubordinateUser>  subordinateList) {
        return managerUserRepository.findByUserID(id)
                .flatMap(newMU -> {
                    newMU.setSubordinateList(subordinateList);
                    return managerUserRepository.save(newMU);
                });
    }

    public Mono<ManagerUser> updateManagerUserUncheckedTaskList(String id, Map<String, Task> uncheckedTasksList) {
        return managerUserRepository.findByUserID(id)
                .flatMap(newMU -> {
                    newMU.setUncheckedTasksList(uncheckedTasksList);
                    return managerUserRepository.save(newMU);
                });
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
                Mono<ManagerUser> newMU = getByUserID(user.getUserID());
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
                Mono<ManagerUser> newMU = getByUserID(manager.getUserID());
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
                Mono<ManagerUser> newMU = getByUserID(manager.getUserID());
                newMU = updateManagerUserTaskList(manager.getUserID(), manager.getLocalUserTaskList());
            }
        }
    }

    public void deleteTaskFromUncheckedTaskList(ManagerUser manager, String taskID) {
        if (manager.getUncheckedTasksList().containsKey(taskID)) {
            manager.getUncheckedTasksList().remove(taskID);

            // update DB
            Mono<ManagerUser> newMU = getByUserID(manager.getUserID());
            newMU = updateManagerUserUncheckedTaskList(manager.getUserID(), manager.getUncheckedTasksList());
        }
    }

    public void addSubordinateToManager(ManagerUser manager, SubordinateUser su) { // suppose that subordinate knows about his manager
        manager.getSubordinateList().putIfAbsent(su.getUserID(), su);

        // update DB
        Mono<ManagerUser> newMU = getByUserID(manager.getUserID());
        newMU = updateManagerUserSubordinateList(manager.getUserID(), manager.getSubordinateList());
    }

    public Flux<Task> getManagerTaskList(String managerID) {
        return getByUserID(managerID)
                .flatMapMany(manager -> {
                    Map<String, Task> managerTasksList = manager.getLocalUserTaskList();
                    return Flux.fromIterable(managerTasksList.values());
                });
    }

    public Flux<SubordinateUser> getSubordinateList(String managerID) {
        return getByUserID(managerID)
                .flatMapMany(manager -> {
                    Map<String, SubordinateUser> managerTasksList = manager.getSubordinateList();
                    return Flux.fromIterable(managerTasksList.values());
                });
    }

    public Flux<Task> getUncheckedTaskList(String managerID) {
        return getByUserID(managerID)
                .flatMapMany(manager -> {
                    Map<String, Task> managerTasksList = manager.getUncheckedTasksList();
                    return Flux.fromIterable(managerTasksList.values());
                });
    }

    public void addToUncheckedTasksListOfManager(ManagerUser manager, Task task) {  // this method will be used by any subordinate who wants to send task request
        manager.getUncheckedTasksList().putIfAbsent(task.getTaskID(), task);

        // update DB
        Mono<ManagerUser> newMU = getByUserID(manager.getUserID());
        newMU = updateManagerUserUncheckedTaskList(manager.getUserID(), manager.getUncheckedTasksList());
    }

    public void approveTaskInUncheckedTasksListOfManager(ManagerUser manager, Task task) {
        if (manager.getUncheckedTasksList().containsKey(task.getTaskID())) {
            SubordinateUser subordinateUser = subordinateTasksService.getByUserID(task.getExecutorID()).block();
            subordinateUser.setScore(subordinateUser.getScore() + taskValue.get(task.getPriority()));

            // update DB
            subordinateUser = subordinateTasksService.updateSubordinateUserScore(subordinateUser.getUserID(), subordinateUser.getScore()).block();
            // in order to sync subordinate's score, update it in object
            updateSubordinateScoreInSubordinateList(manager.getUserID(), subordinateUser.getUserID());

            manager.getUncheckedTasksList().remove(task.getTaskID());

            // update DB
            Mono<ManagerUser> newMU = getByUserID(manager.getUserID());
            newMU = updateManagerUserUncheckedTaskList(manager.getUserID(), manager.getUncheckedTasksList());
        }
    }

    public void updateSubordinateScoreInSubordinateList(String managerID, String subordinateID) {
        Mono<ManagerUser> manager = getByUserID(managerID);
        Mono<SubordinateUser> subordinate = subordinateTasksService.getByUserID(subordinateID);
        manager.block().getSubordinateList().get(subordinateID).setScore(subordinate.block().getScore());
        manager = updateManagerUserSubordinateList(managerID, manager.block().getSubordinateList());
    }

    public void declineTaskInUncheckedTasksListOfManager(ManagerUser manager, Task task) { // changes requested
        if (manager.getUncheckedTasksList().containsKey(task.getTaskID())) {
            SubordinateUser subordinateUser = subordinateTasksService.getByUserID(task.getExecutorID()).block();
            assignTaskToSubordinateOfManager(manager, task, subordinateUser); // send back
            // concerning DB update, see assignTaskToSubordinateOfManager() method

            manager.getUncheckedTasksList().remove(task.getTaskID());

            // update DB
            Mono<ManagerUser> newMU = getByUserID(manager.getUserID());
            newMU = updateManagerUserUncheckedTaskList(manager.getUserID(), manager.getUncheckedTasksList());
        }
    }

    public void addTaskToUser(User user, Task task) {  // both to manager or subordinate. this method can be moved to AdminService e.g.
        task.setCompleted(false);
        task.setExecutorID(user.getUserID());
        user.getLocalUserTaskList().put(task.getTaskID(), task);

        // update DB
        if (user instanceof ManagerUser)
            user = updateManagerUserTaskList(user.getUserID(), user.getLocalUserTaskList()).block();
        else if (user instanceof SubordinateUser)
            user = subordinateTasksService.updateSubordinateUserTaskList(user.getUserID(), user.getLocalUserTaskList()).block();
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
        ManagerUser manager = getByUserID(managerID).block();
        Predicate<Task> isTaskUrgent = task -> task.getPriority() == PriorityType.URGENT;
        List<SubordinateUser> returnList = new LinkedList<>(Arrays.asList(selectSubordinatesWithDefiniteTaskType(manager, isTaskUrgent)));
        return returnList;
    }

    public SubordinateUser[] selectSubordinatesWithDefiniteTaskType(ManagerUser manager, Predicate<Task> taskPredicate) {
        Predicate<SubordinateUser> subordinateUserPredicate = new Predicate<SubordinateUser>() {
            @Override
            public boolean test(SubordinateUser subordinateUser) {
                // we have to take sub from DB, because reference to sub's local task list into manager's sub list is not being synced with real sub's task list.
                SubordinateUser subordinateFromDB = subordinateTasksService.getByUserID(subordinateUser.getUserID()).block();
                return subordinateFromDB.getLocalUserTaskList().values().stream().anyMatch(taskPredicate);
            }
        };
        Map<String, SubordinateUser> subList = manager.getSubordinateList();
        SubordinateUser[] returnArray = subList.values().stream().filter(subordinateUserPredicate).toArray(SubordinateUser[]::new);
        return returnArray;
    }
}
