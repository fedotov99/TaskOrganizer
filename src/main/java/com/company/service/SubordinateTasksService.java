package com.company.service;

import com.company.model.*;
import com.company.repository.SubordinateUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public SubordinateUser createSubordinateUser(String name, ManagerUser manager, int score, PositionType position) {
        SubordinateUser newSU = new SubordinateUser(name, manager, score, position);
        managerTasksService.addSubordinateToManager(manager, newSU);
        return subordinateUserRepository.save(newSU);
    }

    public SubordinateUser getByUserID(String id) {
        return subordinateUserRepository.findByUserID(id);
    }

    public SubordinateUser getByName(String name) {
        return subordinateUserRepository.findByName(name);
    }

    public List<SubordinateUser> getAll(){
        return subordinateUserRepository.findAll();
    }

    public SubordinateUser updateSubordinateUser(String id, String name, ManagerUser manager, int score, PositionType position) {
        SubordinateUser newSU = subordinateUserRepository.findByUserID(id);
        newSU.setName(name);
        newSU.setManager(manager);
        newSU.setScore(score);
        newSU.setPosition(position);
        managerTasksService.addSubordinateToManager(manager, newSU); // how to process it in database???
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
    public void completeTask (User subordinate, String taskID, String report) {            // implements abstract method in User
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

    public void sendRequestForTaskApprovalToManager(SubordinateUser subordinate, Task task) {
        managerTasksService.addToUncheckedTasksListOfManager(subordinate.getManager(), task);
        deleteTaskFromLocalUserTaskList(subordinate, task.getTaskID()); // subordinate thinks that task is ready (until manager doesn't decline)
        // concerning DB update, see deleteTaskFromLocalUserTaskList() method
    }

}
