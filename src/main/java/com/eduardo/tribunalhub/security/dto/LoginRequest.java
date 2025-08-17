package com.eduardo.tribunalhub.security.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String senha;
}
