package com.eduardo.tribunalhub.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String tipo;
    private Long userId;
    private String nome;
    private String email;
    private String cargo;
}
