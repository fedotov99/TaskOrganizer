package com.company.repository;

import com.company.model.ManagerUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface ManagerUserRepository extends ReactiveMongoRepository<ManagerUser, String> {
    public Mono<ManagerUser> findByUserID(String id);
    public Mono<ManagerUser> findByEmail(String email);
    public Flux<ManagerUser> findByName(String name);
}
