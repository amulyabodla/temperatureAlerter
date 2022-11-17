package com.sample.temperaturecheck;

import java.util.Arrays;
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
    public ErrorResponse getDevices(){
        ErrorResponse errorResponse = new ErrorResponse();
        if (errorRepository.findAll().size() > 0)
            errorResponse.setErrors(Arrays.asList(errorRepository.findAll().get(0).getError_txt().split(",")));
        return errorResponse;
    }

    @RequestMapping(value = "/temp", method = RequestMethod.DELETE)
    public void clearDevicesCollection(){
        deviceRepo.deleteAll();
    }

    @RequestMapping(value = "/temp", method = RequestMethod.POST)
    public ResponseEntity<Object> postTemperature(@RequestBody InputData data)
    {
        // - Example `{"data": "365951380:1640995229697:'Temperature':58.48256793121914"}`
        String input[] = data.getData().split(":");
        Device device = new Device();
        Integer deviceID; Double temperature; Long timestamp;

        if (!validateInput(input)) {
            addToErrors(data.getData());
            return ResponseHandler.generateResponse_Bad();
        }
        
        try{
                deviceID = Integer.parseInt(input[0]);
                temperature = Double.parseDouble(input[3]);
                timestamp = Long.parseLong(input[1]);
        }catch(Exception e){
            addToErrors(data.getData());
            return ResponseHandler.generateResponse_Bad();
        }
            device.setDeviceID(deviceID);
            device.setTemperature(temperature);
            device.setTimestamp(timestamp);
            deviceRepo.save(device);  
        
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
        if(inputStrings.length != 4 || inputStrings[0].length() == 0 ||
        inputStrings[1].length() == 0 || inputStrings[2].length() == 0 || 
        inputStrings[3].length() == 0 || !(inputStrings[2].equals("'Temperature'")))
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
        StringBuilder sb = new StringBuilder();
        if(errorRepository.existsByErrorid(1))
        {
            Optional<Error> ent = errorRepository.findById(1);
            sb.append(ent.get().getError_txt()).append(",");
            errorRepository.deleteById(1);
        }
        sb.append(resp);
        error.setErrorid(1);
        error.setError_txt(sb.toString());
        errorRepository.save(error); 
    }

}
