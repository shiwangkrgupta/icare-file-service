package com.icare.file_service.dto.tempcleanup;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TempCleanupConfig {
    @NotNull
    private boolean enabled;
    @NotNull
    private long deleteOlderThanMinutes;
}
