package com.sample.temperaturecheck;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepo extends MongoRepository<Device, Integer> {
    
}
