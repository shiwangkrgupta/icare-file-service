package com.icare.file_service.controller;

import com.icare.file_service.dto.SuccessResponse;
import com.icare.file_service.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<SuccessResponse> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("application") Integer app,
            @RequestParam("doctype") Integer docType
    ) {
        SuccessResponse response = fileStorageService.storeFile(file, app, docType);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/confirm")
    public ResponseEntity<SuccessResponse> confirmFile(@RequestParam("filename") String filename) {
        SuccessResponse response = fileStorageService.confirmFile(filename);
        return ResponseEntity.ok(response);
    }
}
