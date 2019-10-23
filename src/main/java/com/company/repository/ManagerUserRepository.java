package com.company.repository;

import com.company.model.ManagerUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerUserRepository extends MongoRepository<ManagerUser, String> {
    public ManagerUser findByUserID(String id);
    public ManagerUser findByName(String name);
}