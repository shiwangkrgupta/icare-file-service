package com.icare.file_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApplicationUsageDto {
    private Integer applicationId;
    private String applicationName;
    private long usedSpace;
}

