package com.company.service;

import com.company.model.*;
import com.company.repository.SubordinateUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubordinateTasksService extends UserTasksService {
    @Autowired
    protected SubordinateUserRepository subordinateUserRepository;

    public SubordinateUser createSubordinateUser(String name, ManagerUser manager, int score, PositionType position) {
        SubordinateUser newSU = new SubordinateUser(name, manager, score, position);
        ManagerTasksService.addSubordinateToManager(manager, newSU); // how to process it in database???
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
        ManagerTasksService.addSubordinateToManager(manager, newSU); // how to process it in database???
        return subordinateUserRepository.save(newSU);
    }

    public void deleteById(String id) {
        subordinateUserRepository.deleteById(id);
    }

    public void deleteAll() {
        subordinateUserRepository.deleteAll();
    }

    @Override
    public void completeTask (User subordinate, int id, String report) {            // implements abstract method in User
        if (subordinate instanceof SubordinateUser) {
            if (subordinate.getLocalUserTaskList().get(id) != null) {
                subordinate.getLocalUserTaskList().get(id).setReport(report);
                subordinate.getLocalUserTaskList().get(id).setCompleted(true);
                sendRequestForTaskApprovalToManager((SubordinateUser)subordinate, subordinate.getLocalUserTaskList().get(id));
                subordinate.getLocalUserTaskList().remove(id);
            }
        } else {
            System.out.println("Wrong user!");
        }
    }

    private void sendRequestForTaskApprovalToManager(SubordinateUser subordinate, Task task) {
        ManagerTasksService.addToUncheckedTasksListOfManager(subordinate.getManager(), task);
        deleteTask(subordinate, task.getTaskID()); // thinks that it is ready (until manager doesn't decline)
    }

}
