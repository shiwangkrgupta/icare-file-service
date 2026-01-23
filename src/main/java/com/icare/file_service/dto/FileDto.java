package com.icare.file_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDto {
    private String fileName;
    private Long size;
    private String url;
    private LocalDateTime lastModified;
}
