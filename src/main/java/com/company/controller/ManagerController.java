package com.company.controller;

import com.company.model.ManagerUser;
import com.company.model.SubordinateUser;
import com.company.model.Task;
import com.company.model.User;
import com.company.service.ManagerTasksService;
import com.company.service.SubordinateTasksService;
import com.company.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ManagerController {
    @Autowired
    private ManagerTasksService managerTasksService;
    @Autowired
    private SubordinateTasksService subordinateTasksService;
    @Autowired
    private TaskService taskService;

    // create
    @PostMapping("/manager")
    public String createManagerUser(@RequestParam String name) {
        ManagerUser mu = managerTasksService.createManagerUser(name);
        return mu.toString();
    }

    // retrieve
    @GetMapping("/manager/{id}")
    public ManagerUser getManagerUserByID(@PathVariable String id) {
        return managerTasksService.getByUserID(id);
    }

    @GetMapping("/manager/{name}")
    public ManagerUser getManagerUserByName(@PathVariable String name) {
        return managerTasksService.getByName(name);
    }

    @GetMapping("/manager")
    public List<ManagerUser> getAllManagers(){
        return managerTasksService.getAll();
    }

    @GetMapping("manager/{id}/")

    // update
    @PutMapping("/manager/{id}")
    public String updateManagerUser(@PathVariable String id, @RequestParam String name, @RequestParam Map<String, SubordinateUser> subordinateList, @RequestParam Map<String, Task> uncheckedTasksList) {
        ManagerUser mu = managerTasksService.updateManagerUser(id, name, subordinateList, uncheckedTasksList);
        return mu.toString();
    }

    // delete
    @DeleteMapping("/manager/{id}")
    public String delete(@PathVariable String id) {
        managerTasksService.deleteById(id);
        return "Deleted manager id: " + id;
    }

    @DeleteMapping("/manager")
    public String deleteAllManagers(){
        managerTasksService.deleteAll();
        return "Deleted all managers";
    }

    @RequestMapping("/manager/{id}/complete")
    public String completeTask(@PathVariable("id") String managerID, @RequestParam String taskID, @RequestParam String report) {
        ManagerUser mU = managerTasksService.getByUserID(managerID);
        managerTasksService.completeTask(mU, taskID, report);
        return "Completed task " + taskID;
    }

    @RequestMapping("/manager/{id}/delete")
    public String deleteTask(@PathVariable("id") String managerID, @RequestParam String taskID) {
        ManagerUser mU = managerTasksService.getByUserID(managerID);
        managerTasksService.deleteTaskFromLocalUserTaskList(mU, taskID);
        return "Deleted task " + taskID;
    }

    @RequestMapping("/manager/{id}/addSubordinate")
    public String addSubordinateToManager(@PathVariable("id") String managerID, @RequestParam String subordinateID) {
        ManagerUser mU = managerTasksService.getByUserID(managerID);
        SubordinateUser sU = subordinateTasksService.getByUserID(subordinateID);
        managerTasksService.addSubordinateToManager(mU, sU);
        return "Added subordinate " + subordinateID + " to manager " + managerID;
    }

    @RequestMapping("/manager/{id}/addToUnchecked")
    public String addToUncheckedTasksListOfManager(@PathVariable("id") String managerID, @RequestParam String taskID) {
        ManagerUser mU = managerTasksService.getByUserID(managerID);
        Task t = taskService.getByTaskID(taskID);
        managerTasksService.addToUncheckedTasksListOfManager(mU, t);
        return "Added task " + taskID + " to unchecked task list of manager " + managerID;
    }

    @RequestMapping("/manager/{id}/approveTask")
    public String approveTaskInUncheckedTasksListOfManager(@PathVariable("id") String managerID, @RequestParam String taskID) {
        ManagerUser mU = managerTasksService.getByUserID(managerID);
        Task t = taskService.getByTaskID(taskID);
        managerTasksService.approveTaskInUncheckedTasksListOfManager(mU, t);
        return "Approved task " + taskID + " in unchecked task list of manager " + managerID;
    }

    @RequestMapping("/manager/{id}/declineTask")
    public String declineTaskInUncheckedTasksListOfManager(@PathVariable("id") String managerID, @RequestParam String taskID) {
        ManagerUser mU = managerTasksService.getByUserID(managerID);
        Task t = taskService.getByTaskID(taskID);
        managerTasksService.declineTaskInUncheckedTasksListOfManager(mU, t);
        return "Declined task " + taskID + " in unchecked task list of manager " + managerID;
    }

    @RequestMapping("/manager/{id}/assignTaskToSubordinate")
    public String assignTaskToSubordinateOfManager(@PathVariable("id") String managerID, @RequestParam String taskID, @RequestParam String subordinateID) {
        ManagerUser mU = managerTasksService.getByUserID(managerID);
        Task t = taskService.getByTaskID(taskID);
        SubordinateUser sU = subordinateTasksService.getByUserID(subordinateID);
        managerTasksService.assignTaskToSubordinateOfManager(mU, t, sU);
        return "Assigned task " + taskID + " to subordinate " + subordinateID + " of manager " + managerID;
    }

//    public void addTaskToUser(User user, Task task) // this is a technical method and mustn't be treated by controllers
}
