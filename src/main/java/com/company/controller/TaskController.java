package com.company.controller;

import com.company.model.*;
import com.company.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TaskController {
    @Autowired
    private TaskService taskService;

    // create
    @RequestMapping("/create/task")
    public String createTask(@RequestParam String description) {
        Task t = taskService.createTask(description);
        return t.toString();
    }

    @RequestMapping("/create/task")
    public String createTask(@RequestParam String description, PriorityType priority) {
        Task t = taskService.createTask(description, priority);
        return t.toString();
    }

    // retrieve
    @RequestMapping("/get/task")
    public Task getByTaskID(@RequestParam String id) {
        return taskService.getByTaskID(id);
    }

    @RequestMapping("/get/task")
    public Task getByDescription(@RequestParam String description) {
        return taskService.getByDescription(description);
    }

    @RequestMapping("/get/task")
    public List<Task> getByPriority(@RequestParam PriorityType priority) {
        return taskService.getByPriority(priority);
    }

    @RequestMapping("/get/task")
    public List<Task> getByExecutor(@RequestParam User executor) {
        return taskService.getByExecutor(executor);
    }

    @RequestMapping("/getAll/tasks")
    public List<Task> getAllTasks(){
        return taskService.getAll();
    }

    // update
    @RequestMapping("/update/task")
    public String updateTask(@RequestParam String taskID, @RequestParam String description, @RequestParam String report, @RequestParam Boolean completed, @RequestParam PriorityType priority) {
        Task t = taskService.updateTask(taskID, description, report, completed, priority);
        return t.toString();
    }

    // delete
    @RequestMapping("/delete/task")
    public String delete(@RequestParam String id) {
        taskService.deleteByTaskID(id);
        return "Deleted task id: " + id;
    }

    @RequestMapping("/deleteAll/tasks")
    public String deleteAllTasks(){
        taskService.deleteAll();
        return "Deleted all tasks";
    }
}
