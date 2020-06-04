package com.company.service;

import com.company.model.*;
import com.company.repository.ManagerUserRepository;
import com.company.repository.SubordinateUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class SubordinateTasksService extends UserTasksService {
    @Autowired
    protected SubordinateUserRepository subordinateUserRepository;
    @Autowired
    private ManagerTasksService managerTasksService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Mono<SubordinateUser> createSubordinateUser(String name, String email, String password, String managerID, int score, PositionType position) {
        // if not to save here, than newSU's ID will be null, and NPE:
        Mono<SubordinateUser> newSU = subordinateUserRepository.save(new SubordinateUser(name, email, passwordEncoder.encode(password), managerID, score, position));
        Mono<ManagerUser> manager = managerTasksService.getByUserID(managerID);
        managerTasksService.addSubordinateToManager(manager.block(), newSU.block());
        return newSU;
    }

    public Mono<SubordinateUser> getByUserID(String id) {
        return subordinateUserRepository.findByUserID(id);
    }

    public Mono<SubordinateUser> getByName(String name) {
        return subordinateUserRepository.findByName(name);
    }

    public Mono<SubordinateUser> getByEmail(String email) {
        return subordinateUserRepository.findByEmail(email);
    }

    public Flux<SubordinateUser> getAll(){
        return subordinateUserRepository.findAll();
    }

    public Mono<SubordinateUser> updateSubordinateUser(String id, String name, String managerID, int score, PositionType position) {
        return subordinateUserRepository.findByUserID(id)
                .flatMap(newSU -> {
                    newSU.setName(name);
                    newSU.setManagerID(managerID);
                    newSU.setScore(score);
                    newSU.setPosition(position);

                    Mono<ManagerUser> manager = managerTasksService.getByUserID(managerID);
                    managerTasksService.addSubordinateToManager(manager.block(), newSU);
                    return subordinateUserRepository.save(newSU);
                });
    }

    public Mono<SubordinateUser> updateSubordinateUserTaskList(String id, Map<String, Task> localUserTaskList) {
        return subordinateUserRepository.findByUserID(id)
                .flatMap(newSU -> {
                    newSU.setLocalUserTaskList(localUserTaskList);
                    return subordinateUserRepository.save(newSU);
                });
    }

    public Mono<SubordinateUser> updateSubordinateUserScore(String id, int score) {
        return subordinateUserRepository.findByUserID(id)
                .flatMap(newSU -> {
                    newSU.setScore(score);
                    return subordinateUserRepository.save(newSU);
                });
    }

    public void deleteById(String id) {
        subordinateUserRepository.deleteById(id);
    }

    public void deleteAll() {
        subordinateUserRepository.deleteAll();
    }

    @Override
    public void updateTaskInLocalUserTaskList(User user, String taskID) {
        if (user instanceof SubordinateUser) {
            if (user.getLocalUserTaskList().containsKey(taskID)) {
                user.getLocalUserTaskList().remove(taskID);
                Task t = taskService.getByTaskID(taskID);
                user.getLocalUserTaskList().put(t.getTaskID(), t);

                // update DB
                Mono<SubordinateUser> newSU = getByUserID(user.getUserID());
                newSU = updateSubordinateUserTaskList(user.getUserID(), user.getLocalUserTaskList());
            }
        } else {
            System.out.println("Wrong user!");
        }
    }

    @Override
    public void completeTask (User subordinate, String taskID, String report) {  // implements abstract method in User
        if (subordinate instanceof SubordinateUser) {
            if (subordinate.getLocalUserTaskList().get(taskID) != null) {
                subordinate.getLocalUserTaskList().get(taskID).setReport(report);
                subordinate.getLocalUserTaskList().get(taskID).setCompleted(true);
                sendRequestForTaskApprovalToManager((SubordinateUser)subordinate, subordinate.getLocalUserTaskList().get(taskID));
                subordinate.getLocalUserTaskList().remove(taskID);

                // update DB
                Task nT = taskService.getByTaskID(taskID);
                nT = taskService.updateTaskReportAndCompleted(taskID, report,true);
                Mono<SubordinateUser> newSU = getByUserID(subordinate.getUserID());
                newSU = updateSubordinateUserTaskList(subordinate.getUserID(), subordinate.getLocalUserTaskList());
            }
        } else {
            System.out.println("Wrong user!");
        }
    }

    @Override
    public void deleteTaskFromLocalUserTaskList(User subordinate, String taskID) {
        if (subordinate instanceof SubordinateUser) {
            if (subordinate.getLocalUserTaskList().containsKey(taskID)) {
                subordinate.getLocalUserTaskList().remove(taskID);

                // update DB
                Mono<SubordinateUser> newSU = getByUserID(subordinate.getUserID());
                newSU = updateSubordinateUserTaskList(subordinate.getUserID(), subordinate.getLocalUserTaskList());
            }
        } else {
            System.out.println("Wrong user!");
        }
    }

    public Flux<Task> getSubordinateTaskList(String subordinateID) {
        return getByUserID(subordinateID)
                .flatMapMany(subordinate -> {
                    Map<String, Task> subordinateTasksList = subordinate.getLocalUserTaskList();
                    return Flux.fromIterable(subordinateTasksList.values());
                });
    }

    public void sendRequestForTaskApprovalToManager(SubordinateUser subordinate, Task task) {
        Mono<ManagerUser> manager = managerTasksService.getByUserID(subordinate.getManagerID());
        managerTasksService.addToUncheckedTasksListOfManager(manager.block(), task);
        deleteTaskFromLocalUserTaskList(subordinate, task.getTaskID()); // subordinate thinks that task is ready (until manager doesn't decline)
        // concerning DB update, see deleteTaskFromLocalUserTaskList() method
    }

}
