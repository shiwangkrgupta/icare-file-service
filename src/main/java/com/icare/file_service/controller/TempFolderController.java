package com.icare.file_service.controller;
import com.icare.file_service.dto.TempCleanupResultDto;
import com.icare.file_service.dto.TempFolderStatsDto;
import com.icare.file_service.service.TempFolderService;
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
}

