package com.icare.file_service.constansts;

import java.util.HashMap;
import java.util.Map;

public class DoctypeMap {
    public static Map<Integer, String> doctypeMap = Map.of(
            1, "patientimage",
            2, "employeeimage",
            3, "employeesign",
            4, "companylogo",
            5, "pathologyheader",
            6, "pathologyfooter",
            7, "radiologyheader",
            8, "radiologyfooter",
            9, "radiologyattach1",
            10, "radiologyattach2"
    );
}
