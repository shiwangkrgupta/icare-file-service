package com.icare.file_service.controller;

import com.icare.file_service.dto.DirectoryDto;
import com.icare.file_service.dto.FileDto;
import com.icare.file_service.dto.TempFileDto;
import com.icare.file_service.service.FileExplorerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/files")
public class FileExplorerController {

    @Autowired
    private FileExplorerService fileExplorerService;

    // ================= APPLICATIONS =================

    @GetMapping("/applications")
    public List<DirectoryDto> getApplications() {
        return fileExplorerService.getApplications();
    }

    // ================= DOCTYPES =================

    @GetMapping("/applications/{app}/doctypes")
    public List<DirectoryDto> getDoctypes(@PathVariable String app) {
        return fileExplorerService.getDoctypes(app);
    }

    // ================= FILE TYPES =================

    @GetMapping("/applications/{app}/doctypes/{doctype}/filetypes")
    public List<DirectoryDto> getFileTypes(
            @PathVariable String app,
            @PathVariable String doctype
    ) {
        return fileExplorerService.getFileTypes(app, doctype);
    }

    // ================= FILES =================

    @GetMapping("/applications/{app}/doctypes/{doctype}/filetypes/{filetype}")
    public List<FileDto> getFiles(
            @PathVariable String app,
            @PathVariable String doctype,
            @PathVariable String filetype
    ) {
        return fileExplorerService.getFiles(app, doctype, filetype);
    }

    // ================= TEMP =================

    @GetMapping("/temp")
    public List<TempFileDto> getTempFiles() {
        return fileExplorerService.getTempFiles();
    }
}

