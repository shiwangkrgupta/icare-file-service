package com.icare.file_service.dto.tempcleanup;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TempCleanupResultDto {
    private Integer deletedFiles;
    private Long freedSpace;
}
