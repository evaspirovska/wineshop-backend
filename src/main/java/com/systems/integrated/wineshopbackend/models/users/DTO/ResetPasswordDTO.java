package com.systems.integrated.wineshopbackend.models.users.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResetPasswordDTO {
    private final String email;
}
