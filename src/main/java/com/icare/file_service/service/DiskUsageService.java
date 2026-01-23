package com.icare.file_service.service;

import com.icare.file_service.dto.ApplicationUsageDto;
import com.icare.file_service.dto.DiskStatsDto;

import java.util.List;

public interface DiskUsageService {

    DiskStatsDto getDiskStats();

    List<ApplicationUsageDto> getApplicationUsage();
}

