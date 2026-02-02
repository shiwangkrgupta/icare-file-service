package com.icare.file_service.dto.fileexplorer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DirectoryDto {
    private String name;
    private Long size;              // in bytes
    private LocalDateTime lastModified;
}

