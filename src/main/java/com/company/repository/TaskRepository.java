package com.company.repository;

import com.company.model.ManagerUser;
import com.company.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {
    public Task findByDescription(String description);
}
