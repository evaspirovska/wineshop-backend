package com.systems.integrated.wineshopbackend.models.users.DTO;

import com.systems.integrated.wineshopbackend.models.users.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class UserDTO {

    private final Long id;

    @NotNull
    @NotBlank
    private final String email;

    @NotNull
    @NotBlank
    private final String username;

    @NotNull
    @NotBlank
    private final String password;

    @NotNull
    @NotBlank
    private final String name;

    @NotNull
    @NotBlank
    private final String surname;

    @NotNull
    @NotBlank
    private final Role role;


    private final LocalDateTime dateCreated;
}
