package com.eduardo.tribunalhub.validacao;

import java.util.Objects;
import java.util.regex.Pattern;

public class Email {

    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,}$");

    public static void validar(String email) {
        Objects.requireNonNull(email, "Email não pode ser nulo");
                
        if (email.matches(".*\\s+.*")) {
            throw new IllegalArgumentException("O email não pode conter espaços em branco");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Email inválido: " + email);
        }
    }

}
