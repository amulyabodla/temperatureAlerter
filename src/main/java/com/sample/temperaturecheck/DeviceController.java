package com.sample.temperaturecheck;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeviceController {
    @Autowired
    private DeviceRepo deviceRepo;
    @Autowired
    private ErrorRepository errorRepository;
    @RequestMapping(value = "/errors",method = RequestMethod.GET)
    public List<Error> getDevices(){
       return errorRepository.findAll();
    }

    @RequestMapping(value = "/temp", method = RequestMethod.POST)
    public ResponseEntity<Object> postTemperature(@RequestBody InputData data)
    {
        // - Example `{"data": "365951380:1640995229697:'Temperature':58.48256793121914"}`
        String input[] = data.getData().split(":");
        Device device = new Device();
        Integer deviceID; Double temperature; Long timestamp;
        try{
                deviceID = Integer.parseInt(input[0]);
                temperature = Double.parseDouble(input[3]);
                timestamp = Long.parseLong(input[1]);
        }catch(Exception e){
            System.out.println("Inside try catch");
            addToErrors(data.getData());
           return ResponseHandler.generateResponse_Bad("Successfully added data!");
        }
            device.setDeviceID(deviceID);
            device.setTemperature(temperature);
            device.setTimestamp(timestamp);
            deviceRepo.save(device);  
        if(!validateInput(input)){
            addToErrors(data.getData());
            return ResponseHandler.generateResponse_Bad("Successfully added data!");
        }
        if(checkTemperatureConstraint(temperature)){
            return ResponseHandler.generateResponse_OverTemp(deviceID, timestamp);
        }
        else {
            return ResponseHandler.generateResponse_BelowTemp();
        }
    }

    @RequestMapping(value = "/errors", method = RequestMethod.DELETE)
    public void deleteTemperature(){
        errorRepository.deleteAll();
    }

    public boolean validateInput(String[] inputStrings){
        if(inputStrings.length != 4 || !(inputStrings[2].equals("'Temperature'")))
            return false;
        return true;
    }

    public boolean checkTemperatureConstraint(Double temperature){
        if(temperature > 90)
            return true;
        return false;
    }

    public void addToErrors(String resp){
        Error error = new Error();
       /* StringBuilder sb = new StringBuilder();
        if(errorRepository.findbyError_id(1) != null)
        {
            System.out.println("Inside if condition");
            System.out.println(errorRepository.findById(1).toString());
            Optional<Error> ent = errorRepository.findById(1);
            System.out.println(ent.toString());
            sb.append(ent.get().getError_id());
            errorRepository.deleteById(1);
        }
        else {
            sb.append(resp);
        }
        System.out.println("Adding finally");
        error.setError_id(1);
        error.setError_txt(sb.toString());
        errorRepository.save(error); */

}
