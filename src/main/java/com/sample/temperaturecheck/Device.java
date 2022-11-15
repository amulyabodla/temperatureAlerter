package com.sample.temperaturecheck;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("device")
@Data
public class Device {
    private Integer deviceID;
    private Long timestamp;
    private Double temperature;

}
