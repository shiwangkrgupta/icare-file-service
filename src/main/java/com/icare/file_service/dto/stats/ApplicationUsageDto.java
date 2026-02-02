package com.icare.file_service.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApplicationUsageDto {
    private Integer applicationId;
    private String applicationName;
    private long usedSpace;
}

