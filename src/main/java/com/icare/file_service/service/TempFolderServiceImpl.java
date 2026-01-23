package com.icare.file_service.service;

import com.icare.file_service.dto.TempCleanupResultDto;
import com.icare.file_service.dto.TempFolderStatsDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TempFolderServiceImpl implements TempFolderService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private Path tempPath() {
        return Paths.get(uploadDir, "temp");
    }

    // ================= STATS =================

    @Override
    public TempFolderStatsDto getTempFolderStats() {
        Path tempPath = tempPath();

        if (!Files.exists(tempPath)) {
            return new TempFolderStatsDto(0, 0, null, null);
        }

        try {
            List<Path> files = Files.list(tempPath)
                    .filter(Files::isRegularFile)
                    .toList();

            if (files.isEmpty()) {
                return new TempFolderStatsDto(0, 0, null, null);
            }

            long totalSize = files.stream().mapToLong(this::safeSize).sum();

            List<LocalDateTime> times = files.stream()
                    .map(this::lastModified)
                    .sorted()
                    .toList();

            return new TempFolderStatsDto(
                    totalSize,
                    files.size(),
                    times.get(0),
                    times.get(times.size() - 1)
            );

        } catch (IOException e) {
            throw new RuntimeException("Failed to calculate temp folder stats", e);
        }
    }

    // ================= CLEAR ALL =================

    @Override
    public TempCleanupResultDto clearTempFolder() {
        return deleteFiles(Files.exists(tempPath())
                ? listFiles(tempPath())
                : List.of());
    }

    // ================= CLEAR OLD =================

    @Override
    public TempCleanupResultDto clearTempOlderThanHours(int hours) {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(hours);

        List<Path> files = listFiles(tempPath()).stream()
                .filter(p -> Objects.requireNonNull(lastModified(p)).isBefore(cutoff))
                .collect(Collectors.toList());

        return deleteFiles(files);
    }

    @Override
    public TempCleanupResultDto deleteTempFile(String fileName) {
        Path filePath = tempPath().resolve(fileName);

        if (!Files.exists(filePath) || !Files.isRegularFile(filePath)) {
            throw new RuntimeException("Temp file not found: " + fileName);
        }

        try {
            long size = Files.size(filePath);
            Files.delete(filePath);
            return new TempCleanupResultDto(1, size);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete temp file: " + fileName, e);
        }
    }

    // ================= HELPERS =================

    private List<Path> listFiles(Path path) {
        try {
            return Files.list(path)
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to list temp files", e);
        }
    }

    private TempCleanupResultDto deleteFiles(List<Path> files) {
        long freed = 0;
        int count = 0;

        for (Path file : files) {
            try {
                long size = Files.size(file);
                Files.deleteIfExists(file);
                freed += size;
                count++;
            } catch (IOException ignored) {
            }
        }

        return new TempCleanupResultDto(count, freed);
    }

    private long safeSize(Path path) {
        try {
            return Files.size(path);
        } catch (IOException e) {
            return 0L;
        }
    }

    private LocalDateTime lastModified(Path path) {
        try {
            return LocalDateTime.ofInstant(
                    Files.getLastModifiedTime(path).toInstant(),
                    ZoneId.systemDefault()
            );
        } catch (IOException e) {
            return null;
        }
    }
}
