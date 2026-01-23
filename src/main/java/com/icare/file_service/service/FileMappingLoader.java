package com.icare.file_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icare.file_service.dto.FileMappingConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
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

