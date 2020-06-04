package com.company.controller;

import com.company.model.ManagerUser;
import com.company.model.SubordinateUser;
import com.company.model.Task;
import com.company.service.ManagerTasksService;
import com.company.service.SubordinateTasksService;
import com.company.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class ManagerController {
    @Autowired
    private ManagerTasksService managerTasksService;
    @Autowired
    private SubordinateTasksService subordinateTasksService;
    @Autowired
    private TaskService taskService;

    // retrieve
    @GetMapping("/manager/{id}")
    public Mono<ManagerUser> getManagerUserByID(@PathVariable String id) {
        return managerTasksService.getByUserID(id);
    }

    @GetMapping("/manager")
    public Flux<ManagerUser> getAllManagers(){
        return managerTasksService.getAll();
    }

    // update
    @PutMapping("/manager/{id}")
    public Mono<ManagerUser> updateManagerUser(@PathVariable String id, @RequestBody ManagerUser manager) {
        return managerTasksService.updateManagerUser(id, manager.getName(), manager.getSubordinateList(), manager.getUncheckedTasksList());
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

    @PostMapping("/manager/task")
    public Task createTaskByDescriptionAndPriority(@RequestBody Task task) {
        Task t = taskService.createTask(task.getDescription(), task.getPriority(), task.getExecutorID());
        return t;
    }

    @PutMapping("/manager/{id}/update")
    public Task updateTask(@PathVariable("id") String managerID, @RequestBody Task task) {
        ManagerUser mU = managerTasksService.getByUserID(managerID).block();
        // first update task in DB, second update task in localUserTaskList
        Task t = taskService.updateTask(task.getTaskID(), task.getDescription(), task.getReport(), task.isCompleted(), task.getPriority());
        managerTasksService.updateTaskInLocalUserTaskList(mU, task.getTaskID());
        return t;
    }

    @RequestMapping("/manager/{id}/complete")
    public String completeTask(@PathVariable("id") String managerID, @RequestBody Task task) {
        ManagerUser mU = managerTasksService.getByUserID(managerID).block();
        managerTasksService.completeTask(mU, task.getTaskID(), task.getReport());
        return "Completed task " + task.getTaskID();
    }

    @RequestMapping("/manager/{id}/delete")
    public String deleteTask(@PathVariable("id") String managerID, @RequestParam String taskID) {
        ManagerUser mU = managerTasksService.getByUserID(managerID).block();
        // first delete from local user task list, then delete from DB
        managerTasksService.deleteTaskFromLocalUserTaskList(mU, taskID);
        taskService.deleteByTaskID(taskID);
        return "Deleted task " + taskID;
    }

    @RequestMapping("/manager/{id}/addSubordinate")
    public String addSubordinateToManager(@PathVariable("id") String managerID, @RequestParam String subordinateID) {
        ManagerUser mU = managerTasksService.getByUserID(managerID).block();
        SubordinateUser sU = subordinateTasksService.getByUserID(subordinateID).block();
        managerTasksService.addSubordinateToManager(mU, sU);
        return "Added subordinate " + subordinateID + " to manager " + managerID;
    }

    @GetMapping("/manager/{id}/getManagerTaskList")
    public Flux<Task> getManagerTaskList(@PathVariable String id) {
        return managerTasksService.getManagerTaskList(id);
    }

    @GetMapping("/manager/{id}/getUncheckedTaskList")
    public Flux<Task> getUncheckedTaskList(@PathVariable String id) {
        return managerTasksService.getUncheckedTaskList(id);
    }

    @GetMapping("/manager/{id}/getSubordinateList")
    public Flux<SubordinateUser> getSubordinateList(@PathVariable String id) {
        return managerTasksService.getSubordinateList(id);
    }

    @GetMapping("/manager/{id}/getSubordinatesWithUrgentTasks")
    public List<SubordinateUser> getSubordinatesWithUrgentTasks(@PathVariable String id) {
        return managerTasksService.getSubordinatesWithUrgentTasks(id);
    }

    @RequestMapping("/manager/{id}/approveTask")
    public String approveTaskInUncheckedTasksListOfManager(@PathVariable("id") String managerID, @RequestParam String taskID) {
        ManagerUser mU = managerTasksService.getByUserID(managerID).block();
        Task t = taskService.getByTaskID(taskID);
        managerTasksService.approveTaskInUncheckedTasksListOfManager(mU, t);
        return "Approved task " + taskID + " in unchecked task list of manager " + managerID;
    }

    @RequestMapping("/manager/{id}/declineTask")
    public String declineTaskInUncheckedTasksListOfManager(@PathVariable("id") String managerID, @RequestParam String taskID) {
        ManagerUser mU = managerTasksService.getByUserID(managerID).block();
        Task t = taskService.getByTaskID(taskID);
        managerTasksService.declineTaskInUncheckedTasksListOfManager(mU, t);
        return "Declined task " + taskID + " in unchecked task list of manager " + managerID;
    }

    @RequestMapping("/manager/{id}/assignTaskToSubordinate")
    public String assignTaskToSubordinateOfManager(@PathVariable("id") String managerID, @RequestParam String taskID, @RequestParam String subordinateID) {
        ManagerUser mU = managerTasksService.getByUserID(managerID).block();
        Task t = taskService.getByTaskID(taskID);
        SubordinateUser sU = subordinateTasksService.getByUserID(subordinateID).block();
        managerTasksService.assignTaskToSubordinateOfManager(mU, t, sU);
        return "Assigned task " + taskID + " to subordinate " + subordinateID + " of manager " + managerID;
    }

//    public void addTaskToUser(User user, Task task) // this is a technical method and mustn't be treated by controllers
}
