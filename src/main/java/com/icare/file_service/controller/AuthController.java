package com.icare.file_service.controller;


import com.icare.file_service.dto.auth.LoginRequest;
import com.icare.file_service.dto.auth.LoginResponse;
import com.icare.file_service.dto.auth.UserConfigDto;
import com.icare.file_service.security.JwtUtil;
import com.icare.file_service.service.auth.UserConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserConfigService userConfigService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        if (request.getUsername() == null || request.getPassword() == null) {
            throw new RuntimeException("Username and password must not be null");
        }

        // 1. Load user from JSON
        UserConfigDto user = userConfigService.findByUsername(request.getUsername());

        // 2. Validate password
        userConfigService.validatePassword(
                request.getPassword(),
                user.getPassword()
        );

        // 3. Generate JWT
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        // 4. Return token
        return ResponseEntity.ok(
                new LoginResponse(token, user.getUsername())
        );
    }
}

