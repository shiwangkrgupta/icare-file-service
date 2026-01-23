package com.icare.file_service.controller;

import com.icare.file_service.dto.AddConfigItemRequest;
import com.icare.file_service.dto.ConfigItemDto;
import com.icare.file_service.service.FileMappingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Valid
@RestController
@RequestMapping("/api/admin/config")
public class FileConfigController {

    @Autowired
    private FileMappingService fileMappingService;

    // ---------- APPLICATIONS ----------

    @GetMapping("/applications")
    public List<ConfigItemDto> getApplications() {
        return fileMappingService.getApplications();
    }

    @PostMapping("/applications")
    public ConfigItemDto addApplication(@RequestBody @Validated AddConfigItemRequest request) {
        return fileMappingService.addApplication(request.getName());
    }

    // ---------- DOCTYPES ----------

    @GetMapping("/doctypes")
    public List<ConfigItemDto> getDoctypes() {
        return fileMappingService.getDoctypes();
    }

    @PostMapping("/doctypes")
    public ConfigItemDto addDoctype(@RequestBody @Validated AddConfigItemRequest request) {
        return fileMappingService.addDoctype(request.getName());
    }
}

