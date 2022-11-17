package com.sample.temperaturecheck;

import java.util.List;

import lombok.Data;

@Data
public class ErrorResponse {
    private List<String> errors;
}
