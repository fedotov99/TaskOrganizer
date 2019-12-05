package com.company.controller;

import com.company.model.ManagerUser;
import com.company.model.SubordinateUser;
import com.company.model.Task;
import com.company.service.ManagerTasksService;
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
    private ManagerTasksService managerTasksService;
    @Autowired
    private TaskService taskService;

    // this method were moved to RegisterController
/*    // create
    @PostMapping("/subordinate")
    // TODO: get rid of @RequestParam String managerID, because it is in body
    public SubordinateUser createSubordinateUser(@RequestParam String managerID, @RequestBody SubordinateUser subordinate) {
        SubordinateUser su = subordinateTasksService.createSubordinateUser(subordinate.getName(), subordinate.getEmail(), subordinate.getPassword(), managerID, subordinate.getScore(), subordinate.getPosition());
        return su;
    }*/

    // retrieve
    @GetMapping("/subordinate/{id}")
    public SubordinateUser getSubordinateUserByID(@PathVariable String id) {
        return subordinateTasksService.getByUserID(id);
    }

    // TODO: solve ambiguous mapping
/*    @GetMapping("/subordinate/{name}")
    public SubordinateUser getSubordinateUserByName(@PathVariable String name) {
        return subordinateTasksService.getByName(name);
    }*/

    @GetMapping("/subordinate")
    public List<SubordinateUser> getAllSubordinates(){
        return subordinateTasksService.getAll();
    }

    // update
    @PutMapping("/subordinate/{id}")
    public SubordinateUser updateSubordinateUser(@PathVariable String id, @RequestBody SubordinateUser subordinate) {
        SubordinateUser su = subordinateTasksService.updateSubordinateUser(id, subordinate.getName(), subordinate.getManagerID(), subordinate.getScore(), subordinate.getPosition());
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

    @PostMapping("/subordinate/task")
    public Task createTaskByDescriptionAndPriority(@RequestBody Task task) {
        Task t = taskService.createTask(task.getDescription(), task.getPriority(), task.getExecutorID());
        return t;
    }

    @PutMapping("/subordinate/{id}/update")
    public Task updateTask(@PathVariable("id") String subordinateID, @RequestBody Task task) {
        SubordinateUser sU = subordinateTasksService.getByUserID(subordinateID);
        // first update task in DB, second update task in localUserTaskList
        Task t = taskService.updateTask(task.getTaskID(), task.getDescription(), task.getReport(), task.isCompleted(), task.getPriority());
        subordinateTasksService.updateTaskInLocalUserTaskList(sU, task.getTaskID());
        return t;
    }

    @RequestMapping("/subordinate/{id}/complete")
    public String completeTask(@PathVariable("id") String subordinateID, @RequestBody Task task) {
        SubordinateUser sU = subordinateTasksService.getByUserID(subordinateID);
        subordinateTasksService.completeTask(sU, task.getTaskID(), task.getReport());
        return "Completed task " + task.getTaskID();
    }

    @RequestMapping("/subordinate/{id}/delete")
    public String deleteTask(@PathVariable("id") String subordinateID, @RequestParam String taskID) {
        SubordinateUser sU = subordinateTasksService.getByUserID(subordinateID);
        // first delete from local user task list, then delete from DB
        subordinateTasksService.deleteTaskFromLocalUserTaskList(sU, taskID);
        taskService.deleteByTaskID(taskID);
        return "Deleted task " + taskID;
    }

    @GetMapping("/subordinate/{id}/getSubordinateTaskList")
    public List<Task> getSubordinateTaskList(@PathVariable String id) {
        return subordinateTasksService.getSubordinateTaskList(id);
    }

    @GetMapping("/subordinate/getManagerInfo/{id}")
    public ManagerUser getManagerUserInfoByID(@PathVariable String id) {
        return managerTasksService.getByUserID(id);
    }

    // TODO: this controller becomes redundant due to completeTask controller
/*    @RequestMapping("/subordinate/{id}/sendRequestToManager")
    public String sendRequestForTaskApprovalToManager(@PathVariable("id") String subordinateID, @RequestParam String taskID) {
        SubordinateUser sU = subordinateTasksService.getByUserID(subordinateID);
        Task t = taskService.getByTaskID(taskID);
        subordinateTasksService.sendRequestForTaskApprovalToManager(sU, t);
        return "Sent to manager task " + taskID;
    }*/
}
