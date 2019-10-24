package com.company.repository;
import com.company.model.PriorityType;
import com.company.model.Task;
import com.company.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {
    public Task findByTaskID(String id);
    public Task findByDescription(String description);
    public List<Task> findByPriority(PriorityType priority);
    public List<Task> findByExecutor(User executor);
}
