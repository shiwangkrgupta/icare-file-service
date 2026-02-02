package com.icare.file_service.service.temp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icare.file_service.dto.tempcleanup.TempCleanupConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class TempCleanupConfigService {

    private final ObjectMapper mapper;
    private final Path configPath = Paths.get("config/temp-cleanup-config.json");

    public TempCleanupConfig load() {
        try {
            if (Files.notExists(configPath)) {
                Files.createDirectories(configPath.getParent());

                TempCleanupConfig defaultConfig =
                        new TempCleanupConfig(true, 60);

                mapper.writeValue(configPath.toFile(), defaultConfig);
                return defaultConfig;
            }
            return mapper.readValue(configPath.toFile(), TempCleanupConfig.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load temp-cleanup-config.json", e);
        }
    }

    public TempCleanupConfig save(TempCleanupConfig config) {
        try {
            mapper.writeValue(configPath.toFile(), config);
            return config;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save temp cleanup config", e);
        }
    }
}
