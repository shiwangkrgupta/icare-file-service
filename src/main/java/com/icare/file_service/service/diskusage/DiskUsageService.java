package com.icare.file_service.service.diskusage;

import com.icare.file_service.dto.stats.ApplicationUsageDto;
import com.icare.file_service.dto.stats.DiskStatsDto;

import java.util.List;

public interface DiskUsageService {

    DiskStatsDto getDiskStats();

    List<ApplicationUsageDto> getApplicationUsage();
}

