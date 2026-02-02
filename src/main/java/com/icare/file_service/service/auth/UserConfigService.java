package com.icare.file_service.service.auth;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.icare.file_service.dto.auth.UserConfigDto;
import com.icare.file_service.dto.auth.UserConfigFile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class UserConfigService {

    private final ObjectMapper objectMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    private Path getConfigPath() {
        return Paths.get("config", "users.json");
    }

    public UserConfigFile load() {
        try {
            Path path = getConfigPath();

            if (Files.notExists(path)) {
                Files.createDirectories(path.getParent());
                UserConfigFile empty = new UserConfigFile();
                empty.setUsers(java.util.Collections.emptyList());
                objectMapper.writeValue(path.toFile(), empty);
                return empty;
            }

            return objectMapper.readValue(path.toFile(), UserConfigFile.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load users.json", e);
        }
    }

    public UserConfigDto findByUsername(String username) {
        return load().getUsers().stream()
                .filter(UserConfigDto::getEnabled)
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Invalid username or user disabled"));
    }

    public void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new RuntimeException("Invalid username or password");
        }
    }
}

