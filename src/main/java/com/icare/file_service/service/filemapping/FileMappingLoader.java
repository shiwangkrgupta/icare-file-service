package com.icare.file_service.service.filemapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class FileMappingLoader {

    @Autowired
    private FileMappingService fileMappingService;


    public String getApplication(Integer key) {
        return fileMappingService.getApplications()
                .stream()
                .filter(configItemDto -> Objects.equals(configItemDto.getId(), key))
                .findFirst()
                .orElseThrow(()-> new RuntimeException( "Invalid application type or application not present (id=" + key + ")"))
                .getName();
    }

    public String getDoctype(Integer key) {
        return fileMappingService.getDoctypes()
                .stream()
                .filter(configItemDto -> Objects.equals(configItemDto.getId(), key))
                .findFirst()
                .orElseThrow(()-> new RuntimeException( "Invalid doctype or doctype not present (id=" + key + ")"))
                .getName();
    }
}

