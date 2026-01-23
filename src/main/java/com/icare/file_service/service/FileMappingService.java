package com.icare.file_service.service;

import com.icare.file_service.dto.ConfigItemDto;

import java.util.List;

public interface FileMappingService {

    List<ConfigItemDto> getApplications();
    List<ConfigItemDto> getDoctypes();

    ConfigItemDto addApplication(String name);
    ConfigItemDto addDoctype(String name);
}