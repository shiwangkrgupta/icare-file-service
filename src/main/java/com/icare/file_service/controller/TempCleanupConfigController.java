package com.icare.file_service.controller;

import com.icare.file_service.dto.TempCleanupConfig;
import com.icare.file_service.service.TempCleanupConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/temp-cleanup")
@RequiredArgsConstructor
public class TempCleanupConfigController {

    private final TempCleanupConfigService configService;

    @GetMapping
    public TempCleanupConfig getConfig() {
        return configService.load();
    }

    @PutMapping
    public TempCleanupConfig updateConfig(@RequestBody TempCleanupConfig config) {
        return configService.save(config);
    }
}

