package com.icare.file_service.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DiskStatsDto {
    private long totalSpace;
    private long freeSpace;
    private long usedSpace;
}
