package com.icare.file_service.constansts;

import java.util.HashMap;
import java.util.Map;

public class DoctypeMap {
    public static Map<Integer, String> doctypeMap = Map.of(
            1, "patientimage",
            2, "employeeimage",
            3, "employeesign",
            4, "companylogo"
    );
}
