package com.icare.file_service.service;

import com.icare.file_service.dto.TempCleanupResultDto;
import com.icare.file_service.dto.TempFolderStatsDto;

public interface TempFolderService {

    TempFolderStatsDto getTempFolderStats();

    TempCleanupResultDto clearTempFolder();

    TempCleanupResultDto clearTempOlderThanHours(int hours);

    TempCleanupResultDto deleteTempFile(String fileName); // 👈 NEW
}

