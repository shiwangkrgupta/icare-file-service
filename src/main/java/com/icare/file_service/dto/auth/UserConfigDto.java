package com.icare.file_service.dto.auth;


import lombok.Data;

@Data
public class UserConfigDto {
    private Integer id;
    private String username;
    private String password;
    private Boolean enabled;
}
