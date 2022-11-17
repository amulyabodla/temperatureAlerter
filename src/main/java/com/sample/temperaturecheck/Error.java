package com.sample.temperaturecheck;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
@Document("errors")
@Data
public class Error {
    @Id
    private Integer errorid;
    private String error_txt; 
}
