package com.icare.file_service.service;

import com.icare.file_service.dto.ApplicationUsageDto;
import com.icare.file_service.dto.DiskStatsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Service
public class DiskUsageServiceImpl implements DiskUsageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private FileMappingService fileMappingService;


    // ================= DRIVE STATS =================

    @Override
    public DiskStatsDto getDiskStats() {
        try {
            Path path = Paths.get(uploadDir);
            FileStore store = Files.getFileStore(path);

            long total = store.getTotalSpace();
            long free = store.getUsableSpace();
            long used = total - free;

            return new DiskStatsDto(total, free, used);

        } catch (IOException e) {
            throw new RuntimeException("Failed to get disk stats", e);
        }
    }

    // ================= APP USAGE =================

    @Override
    public List<ApplicationUsageDto> getApplicationUsage() {

        return fileMappingService.getApplications()
                .stream()
                .map(app -> {
                    Path appPath = Paths.get(uploadDir, app.getName());
                    long size = Files.exists(appPath) ? folderSize(appPath) : 0;
                    return new ApplicationUsageDto(
                            app.getId(),
                            app.getName(),
                            size
                    );
                })
                .toList();
    }

    // ================= HELPER =================

    private long folderSize(Path path) {
        try {
            return Files.walk(path)
                    .filter(Files::isRegularFile)
                    .mapToLong(p -> {
                        try {
                            return Files.size(p);
                        } catch (IOException e) {
                            return 0L;
                        }
                    })
                    .sum();
        } catch (IOException e) {
            throw new RuntimeException("Failed to calculate folder size", e);
        }
    }
}

