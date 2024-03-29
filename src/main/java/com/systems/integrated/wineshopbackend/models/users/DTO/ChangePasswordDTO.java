package com.systems.integrated.wineshopbackend.models.users.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangePasswordDTO {

    private final Long userId;

    private final String newPassword;
}
