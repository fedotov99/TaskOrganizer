package com.company.service;

import com.company.model.*;
import com.company.repository.ManagerUserRepository;
import com.company.repository.SubordinateUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public SubordinateUser createSubordinateUser(String name, String email, String password, String managerID, int score, PositionType position) {
        // if not to save here, than newSU's ID will be null, and NPE:
        SubordinateUser newSU = subordinateUserRepository.save(new SubordinateUser(name, email, passwordEncoder.encode(password), managerID, score, position));
        ManagerUser manager = managerTasksService.getByUserID(managerID);
        managerTasksService.addSubordinateToManager(manager, newSU);
        return newSU;
    }

    public SubordinateUser getByUserID(String id) {
        return subordinateUserRepository.findByUserID(id);
    }

    public SubordinateUser getByName(String name) {
        return subordinateUserRepository.findByName(name);
    }

    public SubordinateUser getByEmail(String email) {
        return subordinateUserRepository.findByEmail(email);
    }

    public List<SubordinateUser> getAll(){
        return subordinateUserRepository.findAll();
    }

    public SubordinateUser updateSubordinateUser(String id, String name, String managerID, int score, PositionType position) {
        SubordinateUser newSU = subordinateUserRepository.findByUserID(id);
        newSU.setName(name);
        newSU.setManagerID(managerID);
        newSU.setScore(score);
        newSU.setPosition(position);
        ManagerUser manager = managerTasksService.getByUserID(managerID);
        managerTasksService.addSubordinateToManager(manager, newSU);
        return subordinateUserRepository.save(newSU);
    }

    public SubordinateUser updateSubordinateUserTaskList(String id, Map<String, Task> localUserTaskList) {
        SubordinateUser newSU = subordinateUserRepository.findByUserID(id);
        newSU.setLocalUserTaskList(localUserTaskList);
        return subordinateUserRepository.save(newSU);
    }

    public SubordinateUser updateSubordinateUserScore(String id, int score) {
        SubordinateUser newSU = subordinateUserRepository.findByUserID(id);
        newSU.setScore(score);
        return subordinateUserRepository.save(newSU);
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
                SubordinateUser newSU = getByUserID(user.getUserID());
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
                SubordinateUser newSU = getByUserID(subordinate.getUserID());
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
                SubordinateUser newSU = getByUserID(subordinate.getUserID());
                newSU = updateSubordinateUserTaskList(subordinate.getUserID(), subordinate.getLocalUserTaskList());
            }
        } else {
            System.out.println("Wrong user!");
        }
    }

    public List<Task> getSubordinateTaskList(String subordinateID) {
        SubordinateUser subordinate = getByUserID(subordinateID);
        Map<String, Task> subordinateTasksList = subordinate.getLocalUserTaskList();
        return new LinkedList<>(subordinateTasksList.values());
    }

    public void sendRequestForTaskApprovalToManager(SubordinateUser subordinate, Task task) {
        ManagerUser manager = managerTasksService.getByUserID(subordinate.getManagerID());
        managerTasksService.addToUncheckedTasksListOfManager(manager, task);
        deleteTaskFromLocalUserTaskList(subordinate, task.getTaskID()); // subordinate thinks that task is ready (until manager doesn't decline)
        // concerning DB update, see deleteTaskFromLocalUserTaskList() method
    }

}
