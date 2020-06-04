package com.company.repository;

import com.company.model.SubordinateUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface SubordinateUserRepository extends ReactiveMongoRepository<SubordinateUser, String> {
    public Mono<SubordinateUser> findByUserID(String id);
    public Mono<SubordinateUser> findByEmail(String email);
    public Mono<SubordinateUser> findByName(String name);
}
