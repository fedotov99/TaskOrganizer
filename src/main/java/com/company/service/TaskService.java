package com.company.service;

import com.company.model.PriorityType;
import com.company.model.Task;
import com.company.model.User;
import com.company.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    protected TaskRepository taskRepository;

    public Task createTask(String description) {
        return taskRepository.save(new Task(description));
    }

    public Task createTask(String description, PriorityType priority) {
        return taskRepository.save(new Task(description, priority));
    }

    public Task getByTaskID(String id) {
        return taskRepository.findByTaskID(id);
    }

    public Task getByDescription(String description) {
        return taskRepository.findByDescription(description);
    }

    public List<Task> getByPriority(PriorityType priority){
        return taskRepository.findByPriority(priority);
    }

    public List<Task> getByExecutor(User executor){
        return taskRepository.findByExecutor(executor);
    }

    public List<Task> getAll(){
        return taskRepository.findAll();
    }

    public Task updateTask(String taskID, String description, String report, Boolean completed, PriorityType priority) {
        Task nt = taskRepository.findByTaskID(taskID);
        nt.setDescription(description);
        nt.setReport(report);
        nt.setCompleted(completed);
        nt.setPriority(priority);
        return taskRepository.save(nt);
    }

    public void deleteByTaskID(String id) {
        taskRepository.deleteById(id);
    }

    public void deleteAll() {
        taskRepository.deleteAll();
    }
}
