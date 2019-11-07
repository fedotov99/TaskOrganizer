package com.company.controller;

import com.company.model.*;
import com.company.service.SubordinateTasksService;
import com.company.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private SubordinateTasksService subordinateTasksService;

    // create
    /*
    @PostMapping("/task")
    public String createTaskByDescription(@RequestParam String description) {
        Task t = taskService.createTask(description);
        return t.toString();
    }
    */

    @PostMapping("/task")
    public Task createTaskByDescriptionAndPriority(@RequestParam String description, PriorityType priority) {
        Task t = taskService.createTask(description, priority);
        return t;
    }

//    @PostMapping("/task")
//    public Task createTaskByDescription(@RequestParam String description) {
//        Task t = taskService.createTask(description);
//        return t;
//    }

    // retrieve
    @GetMapping("/task/{id}")
    public Task getByTaskID(@PathVariable String id) {
        return taskService.getByTaskID(id);
    }

    @GetMapping("/task/{description}")
    public Task getByDescription(@PathVariable String description) {
        return taskService.getByDescription(description);
    }

    @GetMapping("/task/{priority")
    public List<Task> getByPriority(@PathVariable PriorityType priority) {
        return taskService.getByPriority(priority);
    }

    @GetMapping("/task/byUser/{executorID}")
    public List<Task> getByExecutor(@PathVariable String executorID) {
        SubordinateUser executor = subordinateTasksService.getByUserID(executorID);
        return taskService.getByExecutor(executor);
    }

    @GetMapping("/task")
    public List<Task> getAllTasks(){
        return taskService.getAll();
    }

    // update
    @PutMapping("/task/{id}")
    public String updateTask(@PathVariable("id") String taskID, @RequestParam String description, @RequestParam String report, @RequestParam Boolean completed, @RequestParam PriorityType priority) {
        Task t = taskService.updateTask(taskID, description, report, completed, priority);
        return t.toString();
    }

    // delete
    @DeleteMapping("/task/{id}")
    public String delete(@PathVariable String id) {
        taskService.deleteByTaskID(id);
        return "Deleted task id: " + id;
    }

    @DeleteMapping("/task")
    public String deleteAllTasks(){
        taskService.deleteAll();
        return "Deleted all tasks";
    }
}
