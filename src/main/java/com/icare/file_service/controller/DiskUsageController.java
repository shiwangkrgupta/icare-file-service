package com.icare.file_service.controller;

import com.icare.file_service.dto.stats.ApplicationUsageDto;
import com.icare.file_service.dto.stats.DiskStatsDto;
import com.icare.file_service.service.diskusage.DiskUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/disk")
public class DiskUsageController {

    @Autowired
    private DiskUsageService diskUsageService;


    // ================= DRIVE =================

    @GetMapping("/stats")
    public DiskStatsDto getDiskStats() {
        return diskUsageService.getDiskStats();
    }

    // ================= APPLICATION =================

    @GetMapping("/applications")
    public List<ApplicationUsageDto> getApplicationUsage() {
        return diskUsageService.getApplicationUsage();
    }
}

