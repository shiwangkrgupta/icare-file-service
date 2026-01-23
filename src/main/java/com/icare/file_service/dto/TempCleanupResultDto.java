package com.icare.file_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TempCleanupResultDto {
    private int deletedFiles;
    private long freedSpace;
}
