package com.eduardo.tribunalhub.app.usuario;

public class ValidadorSenha {
    
    public static void validar(String senha) {
        if (senha == null) {
            throw new IllegalArgumentException("A senha não pode ser nula");
        }
        
        if (!senha.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("A senha deve conter pelo menos uma letra minúscula");
        }
        
        if (!senha.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("A senha deve conter pelo menos uma letra maiúscula");
        }
        
        if (!senha.matches(".*[0-9].*")) {
            throw new IllegalArgumentException("A senha deve conter pelo menos um número");
        }
        
        if (!senha.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            throw new IllegalArgumentException("A senha deve conter pelo menos um caractere especial");
        }


    }

}