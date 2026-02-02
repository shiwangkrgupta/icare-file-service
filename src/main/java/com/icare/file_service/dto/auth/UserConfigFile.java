package com.icare.file_service.dto.auth;


import lombok.Data;

import java.util.List;

@Data
public class UserConfigFile {
    private List<UserConfigDto> users;
}
