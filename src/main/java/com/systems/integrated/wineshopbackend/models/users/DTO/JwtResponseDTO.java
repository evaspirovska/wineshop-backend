package com.systems.integrated.wineshopbackend.models.users.DTO;

import lombok.Getter;

@Getter
public class JwtResponseDTO {

    private final Long id;
    private final String username;
    private final String name;
    private final String surname;
    private final String email;
    private final String token;
    private final String role;
    private final String type = "Bearer";

    public JwtResponseDTO(Long id, String username, String name, String surname, String email, String token,
                          String role) {
        this.id = id;
        this.username = username;
        this.name = name;
        this. surname = surname;
        this.email = email;
        this.token = token;
        this.role = role;
    }
}
