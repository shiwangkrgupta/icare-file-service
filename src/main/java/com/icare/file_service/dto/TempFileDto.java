package com.icare.file_service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TempFileDto {
    private String fileName;
    private Long size;
    private LocalDateTime lastModified;
    private String url;

}
