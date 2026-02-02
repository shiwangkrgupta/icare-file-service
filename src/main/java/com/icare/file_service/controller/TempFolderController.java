package com.icare.file_service.controller;

import com.icare.file_service.dto.tempcleanup.TempCleanupResultDto;
import com.icare.file_service.dto.stats.TempFolderStatsDto;
import com.icare.file_service.service.temp.TempFolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/temp")
public class TempFolderController {

    @Autowired
    private TempFolderService tempFolderService;


    // ================= STATS =================

    @GetMapping("/stats")
    public TempFolderStatsDto getStats() {
        return tempFolderService.getTempFolderStats();
    }

    // ================= CLEANUP =================

    @DeleteMapping("/clear")
    public TempCleanupResultDto clearAll() {
        return tempFolderService.clearTempFolder();
    }

    @DeleteMapping("/clear/{hours}")
    public TempCleanupResultDto clearOlderThan(@PathVariable int hours) {
        return tempFolderService.clearTempOlderThanHours(hours);
    }

    // ================= DELETE SINGLE =================

    @DeleteMapping("/files/{fileName}")
    public TempCleanupResultDto deleteTempFile(@PathVariable String fileName) {
        return tempFolderService.deleteTempFile(fileName);
    }
}

