package com.icare.file_service.constansts;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ApplicationMap {
    private  Map<Integer, String> applicationMap;

    public ApplicationMap(){
        applicationMap = new HashMap<>();
        applicationMap.put(1, "lms");
        applicationMap.put(2, "opd");
    }

    public Map<Integer, String> getApplicationMap(){
        return applicationMap;
    }
}
