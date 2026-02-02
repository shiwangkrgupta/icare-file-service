package com.icare.file_service.controller;

import com.icare.file_service.dto.tempcleanup.TempCleanupConfig;
import com.icare.file_service.service.temp.TempCleanupConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/temp-cleanup")
@RequiredArgsConstructor
public class TempCleanupConfigController {

    private final TempCleanupConfigService configService;

    @GetMapping("/getConfig")
    public TempCleanupConfig getConfig() {
        return configService.load();
    }

    @PutMapping("/updateConfig")
    public TempCleanupConfig updateConfig(@RequestBody TempCleanupConfig config) {
        return configService.save(config);
    }
}

