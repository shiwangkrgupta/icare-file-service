package com.icare.file_service.service;

import com.icare.file_service.dto.DirectoryDto;
import com.icare.file_service.dto.FileDto;
import com.icare.file_service.dto.TempFileDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileExplorerServiceImpl implements FileExplorerService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.base-url}")
    private String baseUrl;

    // ---------------- APPLICATIONS ----------------

    @Override
    public List<DirectoryDto> getApplications() {
        return listDirectories(Paths.get(uploadDir));
    }

    // ---------------- DOCTYPES ----------------

    @Override
    public List<DirectoryDto> getDoctypes(String application) {
        Path appPath = Paths.get(uploadDir, application);
        validateDirectory(appPath, "Application not found");
        return listDirectories(appPath);
    }

    // ---------------- FILE TYPES ----------------

    @Override
    public List<DirectoryDto> getFileTypes(String application, String doctype) {
        Path doctypePath = Paths.get(uploadDir, application, doctype);
        validateDirectory(doctypePath, "Doctype not found");
        return listDirectories(doctypePath);
    }

    // ---------------- FILES ----------------

    @Override
    public List<FileDto> getFiles(String application, String doctype, String fileType) {
        Path fileTypePath = Paths.get(uploadDir, application, doctype, fileType);
        validateDirectory(fileTypePath, "FileType not found");

        try (Stream<Path> paths = Files.list(fileTypePath)) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(file -> mapToFileDto(application, doctype, fileType, file))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read files", e);
        }
    }

    // ---------------- TEMP FILES ----------------

    @Override
    public List<TempFileDto> getTempFiles() {
        Path tempPath = Paths.get(uploadDir, "temp");
        validateDirectory(tempPath, "Temp folder not found");

        try (Stream<Path> paths = Files.list(tempPath)) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(this::mapToTempDto)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read temp files", e);
        }
    }

    // ================== HELPERS ==================

    private List<DirectoryDto> listDirectories(Path path) {
        try (Stream<Path> paths = Files.list(path)) {
            return paths
                    .filter(Files::isDirectory)
                    .map(dir -> new DirectoryDto(
                            dir.getFileName().toString(),
                            dir.toString()
                    ))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to list directories", e);
        }
    }

    private FileDto mapToFileDto(String app, String doctype, String fileType, Path file) {
        try {
            return new FileDto(
                    file.getFileName().toString(),
                    Files.size(file),
                    baseUrl + "/" + app + "/" + doctype + "/" + fileType + "/" + file.getFileName(),
                    LocalDateTime.ofInstant(
                            Files.getLastModifiedTime(file).toInstant(),
                            ZoneId.systemDefault()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to map file", e);
        }
    }

    private TempFileDto mapToTempDto(Path file) {
        try {
            TempFileDto dto = new TempFileDto();
            dto.setFileName(file.getFileName().toString());
            dto.setSize(Files.size(file));
            dto.setUrl(baseUrl + "/" + "temp/" + file.getFileName());
            dto.setLastModified(
                    LocalDateTime.ofInstant(
                            Files.getLastModifiedTime(file).toInstant(),
                            ZoneId.systemDefault()
                    )
            );
            return dto;
        } catch (IOException e) {
            throw new RuntimeException("Failed to map temp file", e);
        }
    }

    private void validateDirectory(Path path, String message) {
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            throw new RuntimeException(message);
        }
    }
}
