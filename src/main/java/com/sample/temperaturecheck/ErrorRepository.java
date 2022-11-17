package com.sample.temperaturecheck;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ErrorRepository extends MongoRepository<Error, Integer> {
    boolean existsByErrorid(Integer errorId);
 }
