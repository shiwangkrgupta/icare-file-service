package com.icare.file_service.constansts;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DoctypeMap {
    private  Map<Integer, String> doctypeMap;

    DoctypeMap(){
        doctypeMap = new HashMap<>();
        doctypeMap.put(1, "patientimage");
        doctypeMap.put(2, "employeeimage");
        doctypeMap.put(3, "employeesign");
        doctypeMap.put(4, "companylogo");
        doctypeMap.put(5, "pathologyheader");
        doctypeMap.put(6, "pathologyfooter");
        doctypeMap.put(7, "radiologyheader");
        doctypeMap.put(8, "radiologyfooter");
        doctypeMap.put(9, "radiologyattach1");
        doctypeMap.put(10, "radiologyattach2");
        doctypeMap.put(11, "radiologyreport");
        doctypeMap.put(12, "pathologyreport");
        doctypeMap.put(13, "doctorimage");
    }

    public Map<Integer, String> getDoctypeMap() {
        return doctypeMap;
    }
}
