package com.icare.file_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icare.file_service.dto.ConfigItemDto;
import com.icare.file_service.dto.FileMappingConfig;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class FileMappingServiceImpl implements FileMappingService {

    private final Path configPath = Paths.get("config", "mapping.json");
    private final ObjectMapper mapper = new ObjectMapper();

    private FileMappingConfig load() {
        try {
            // 1. Create config directory if missing
            if (!Files.exists(configPath.getParent())) {
                Files.createDirectories(configPath.getParent());
            }

            // 2. If file does not exist, create default config
            if (!Files.exists(configPath)) {
                FileMappingConfig defaultConfig = new FileMappingConfig();
                defaultConfig.setApplications(new ArrayList<>());
                defaultConfig.setDoctypes(new ArrayList<>());

                mapper.writerWithDefaultPrettyPrinter()
                        .writeValue(configPath.toFile(), defaultConfig);

                return defaultConfig;
            }

            // 3. Load existing file
            return mapper.readValue(configPath.toFile(), FileMappingConfig.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load file-mapping.json", e);
        }
    }

    private void save(FileMappingConfig config) {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(configPath.toFile(), config);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save file-mapping.json", e);
        }
    }

    // ================= READ =================

    @Override
    public List<ConfigItemDto> getApplications() {
        FileMappingConfig config = load();
        return toDtoList(config.getApplications());
    }

    @Override
    public List<ConfigItemDto> getDoctypes() {
        FileMappingConfig config = load();
        return toDtoList(config.getDoctypes());
    }

    // ================= WRITE =================

    @Override
    public ConfigItemDto addApplication(String name) {
        FileMappingConfig config = load();

        if (config.getApplications().contains(name)) {
            throw new RuntimeException("Application already exists");
        }

        config.getApplications().add(name);
        save(config);

        int id = config.getApplications().size();
        return new ConfigItemDto(id, name);
    }

    @Override
    public ConfigItemDto addDoctype(String name) {
        FileMappingConfig config = load();

        if (config.getDoctypes().contains(name)) {
            throw new RuntimeException("Doctype already exists");
        }

        config.getDoctypes().add(name);
        save(config);

        int id = config.getDoctypes().size();
        return new ConfigItemDto(id, name);
    }

    // ================= HELPERS =================

    private List<ConfigItemDto> toDtoList(List<String> list) {
        return IntStream.range(0, list.size())
                .mapToObj(i -> new ConfigItemDto(i + 1, list.get(i)))
                .toList();
    }
}
