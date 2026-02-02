package com.icare.file_service.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TempFolderStatsDto {
    private long totalSize;
    private int fileCount;
    private LocalDateTime oldestFile;
    private LocalDateTime newestFile;
}
