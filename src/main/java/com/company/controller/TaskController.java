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
    public Task createTaskByDescriptionAndPriority(@RequestBody Task task) {
        Task t = taskService.createTask(task.getDescription(), task.getPriority(), task.getExecutorID());
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

    // TODO: solve ambiguous mapping
    /*@GetMapping("/task/{description}")
    public Task getByDescription(@PathVariable String description) {
        return taskService.getByDescription(description);
    }

    @GetMapping("/task/{priority")
    public List<Task> getByPriority(@PathVariable PriorityType priority) {
        return taskService.getByPriority(priority);
    }*/

    @GetMapping("/task/byUser/{executorID}")
    public List<Task> getByExecutor(@PathVariable String executorID) {
        return taskService.getByExecutorID(executorID);
    }

    @GetMapping("/task")
    public List<Task> getAllTasks(){
        return taskService.getAll();
    }

/*    // update
    @PutMapping("/task/{id}")
    public Task updateTask(@PathVariable String id, @RequestBody String description, @RequestBody String report, @RequestBody Boolean completed, @RequestBody Integer priority) {
        Task t = taskService.updateTask(id, description, report, completed, PriorityType.getPriorityType(priority));
        return t;
    }*/

    @PutMapping("/task/{id}")
    public Task updateTask(@PathVariable String id, @RequestBody Task task) {
        Task t = taskService.updateTask(id, task.getDescription(), task.getReport(), task.isCompleted(), task.getPriority());
        return t;
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
