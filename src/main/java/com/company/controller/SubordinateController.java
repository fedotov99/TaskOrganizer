package com.company.controller;

import com.company.model.ManagerUser;
import com.company.model.PositionType;
import com.company.model.SubordinateUser;
import com.company.model.Task;
import com.company.service.SubordinateTasksService;
import com.company.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SubordinateController {
    @Autowired
    private SubordinateTasksService subordinateTasksService;
    @Autowired
    private TaskService taskService;

    // create
    @PostMapping("/subordinate")
    public String createSubordinateUser(@RequestBody SubordinateUser subordinate) {
        SubordinateUser su = subordinateTasksService.createSubordinateUser(subordinate.getName(), subordinate.getManager(), subordinate.getScore(), subordinate.getPosition());
        return su.toString();
    }

    // retrieve
    @GetMapping("/subordinate/{id}")
    public SubordinateUser getSubordinateUserByID(@PathVariable String id) {
        return subordinateTasksService.getByUserID(id);
    }

    @GetMapping("/subordinate/{name}")
    public SubordinateUser getSubordinateUserByName(@PathVariable String name) {
        return subordinateTasksService.getByName(name);
    }

    @GetMapping("/subordinate")
    public List<SubordinateUser> getAllSubordinates(){
        return subordinateTasksService.getAll();
    }

    // update
    @PutMapping("/subordinate/{id}")
    public SubordinateUser updateSubordinateUser(@PathVariable String id, @RequestBody SubordinateUser subordinate) {
        SubordinateUser su = subordinateTasksService.updateSubordinateUser(id, subordinate.getName(), subordinate.getManager(), subordinate.getScore(), subordinate.getPosition());
        return su;
    }

    // delete
    @DeleteMapping("/subordinate/{id}")
    public String delete(@PathVariable String id) {
        subordinateTasksService.deleteById(id);
        return "Deleted subordinate id: " + id;
    }

    @DeleteMapping("/subordinate")
    public String deleteAllSubordinates(){
        subordinateTasksService.deleteAll();
        return "Deleted all subordinates";
    }

    @RequestMapping("/subordinate/{id}/complete")
    public String completeTask(@PathVariable("id") String subordinateID, @RequestParam String taskID, @RequestParam String report) {
        SubordinateUser sU = subordinateTasksService.getByUserID(subordinateID);
        subordinateTasksService.completeTask(sU, taskID, report);
        return "Completed task " + taskID;
    }

    @RequestMapping("/subordinate/{id}/delete")
    public String deleteTask(@PathVariable("id") String subordinateID, @RequestParam String taskID) {
        SubordinateUser sU = subordinateTasksService.getByUserID(subordinateID);
        subordinateTasksService.deleteTaskFromLocalUserTaskList(sU, taskID);
        return "Deleted task " + taskID;
    }

    @RequestMapping("/subordinate/{id}/sendRequestToManager")
    public String sendRequestForTaskApprovalToManager(@PathVariable("id") String subordinateID, @RequestParam String taskID) {
        SubordinateUser sU = subordinateTasksService.getByUserID(subordinateID);
        Task t = taskService.getByTaskID(taskID);
        subordinateTasksService.sendRequestForTaskApprovalToManager(sU, t);
        return "Deleted task " + taskID;
    }
}
