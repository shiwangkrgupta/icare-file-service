package com.icare.file_service.dto.mapping;

import lombok.Data;
import java.util.List;

@Data
public class FileMappingConfig {
    private List<String> applications;
    private List<String> doctypes;
}