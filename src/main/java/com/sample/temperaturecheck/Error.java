package com.sample.temperaturecheck;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
@Document("errors")
@Data
public class Error {
    private Integer error_id;
    private String error_txt; 
}
