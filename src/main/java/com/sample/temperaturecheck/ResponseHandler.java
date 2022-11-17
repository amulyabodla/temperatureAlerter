package com.sample.temperaturecheck;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {
    public static ResponseEntity<Object> generateResponse_Bad(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("error", "bad request");
        return new ResponseEntity<Object>(map,HttpStatus.BAD_REQUEST);
}

    public static ResponseEntity<Object> generateResponse_OverTemp(Integer deviceID, Long timestamp){
        Map<String, Object> map = new HashMap<String, Object>();
        String time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(timestamp);

        map.put("formatted_time", time);
        map.put("device_id",deviceID);
        map.put("overtemp",true);

        return new ResponseEntity<Object>(map,HttpStatus.OK);
    }

    public static ResponseEntity<Object> generateResponse_BelowTemp(){
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        map.put("overtemp", false);
        return new ResponseEntity<Object>(map, HttpStatus.OK);
    }
}