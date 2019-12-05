package com.company.controller;

import com.company.model.*;
import com.company.service.SubordinateTasksService;
import com.company.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


// ATTENTION! It is not recommended to call methods of this controller, because it works with all tasks in database,
// without syncing with local user's data structures (localUserTasksList), except for task creating method,
// which adds task to localUserTasksList just after task has been created in DB.
// This controller can be helpful for admin dashboard (if it is needed) or testing,
// but if we work with user's account, we should call specific user controller's methods
// to be sure that user's local data structures are always up to date.

// UPD: this controller was denied to all. use manager's and subordinate's controller instead of this

@RestController
public class TaskController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private SubordinateTasksService subordinateTasksService;

    // create
    @PostMapping("/task")
    public Task createTaskByDescriptionAndPriority(@RequestBody Task task) {
        Task t = taskService.createTask(task.getDescription(), task.getPriority(), task.getExecutorID());
        return t;
    }

    // retrieve
    @GetMapping("/task/{id}")
    public Task getByTaskID(@PathVariable String id) {
        return taskService.getByTaskID(id);
    }

    @GetMapping("/task/byUser/{executorID}")
    public List<Task> getByExecutor(@PathVariable String executorID) {
        return taskService.getByExecutorID(executorID);
    }

    @GetMapping("/task")
    public List<Task> getAllTasks(){
        return taskService.getAll();
    }

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
