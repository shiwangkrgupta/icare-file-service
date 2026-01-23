package com.icare.file_service.dto;

import java.util.Map;

import lombok.Data;
import java.util.List;

@Data
public class FileMappingConfig {
    private List<String> applications;
    private List<String> doctypes;
}