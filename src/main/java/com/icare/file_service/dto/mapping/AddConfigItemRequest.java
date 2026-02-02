package com.icare.file_service.dto.mapping;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddConfigItemRequest {
    @NotNull
    private String name;
}
