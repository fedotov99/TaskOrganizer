package com.company.repository;

import com.company.model.ManagerUser;
import com.company.model.SubordinateUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubordinateUserRepository extends MongoRepository<SubordinateUser, String> {
    public SubordinateUser findByUserID(String id);
    public SubordinateUser findByName(String name);
}