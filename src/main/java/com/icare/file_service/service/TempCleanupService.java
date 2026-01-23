package com.icare.file_service.service;

import com.icare.file_service.dto.TempCleanupConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class TempCleanupService {

    private final TempCleanupConfigService configService;

    @Value("${file.upload-dir}")
    private String uploadDir;


    @Scheduled(fixedDelay = 120 * 60 * 1000)// runs every two hrs
    public void cleanupTempFiles() {
        TempCleanupConfig config = configService.load();

        if (!config.isEnabled()) {
            log.info("Temp cleanup is disabled");
            return;
        }

        Path tempPath = Paths.get(uploadDir + "/temp");
        if (Files.notExists(tempPath)) return;

        long cutoffTime = System.currentTimeMillis()
                - Duration.ofMinutes(config.getDeleteOlderThanMinutes()).toMillis();

        try (Stream<Path> files = Files.walk(tempPath)) {
            files
                    .filter(Files::isRegularFile)
                    .filter(p -> {
                        try {
                            return Files.getLastModifiedTime(p).toMillis() < cutoffTime;
                        } catch (IOException e) {
                            return false;
                        }
                    })
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                            log.info("Deleted temp file: {}", p);
                        } catch (IOException e) {
                            log.error("Failed to delete {}", p, e);
                        }
                    });
        } catch (IOException e) {
            log.error("Temp cleanup failed", e);
        }
    }

}

