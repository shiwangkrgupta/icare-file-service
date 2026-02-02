package com.icare.file_service.service.temp;

import com.icare.file_service.dto.tempcleanup.TempCleanupResultDto;
import com.icare.file_service.dto.stats.TempFolderStatsDto;

public interface TempFolderService {

    TempFolderStatsDto getTempFolderStats();

    TempCleanupResultDto clearTempFolder();

    TempCleanupResultDto clearTempOlderThanHours(int hours);

    TempCleanupResultDto deleteTempFile(String fileName); // 👈 NEW
}

