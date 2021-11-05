package com.systems.integrated.wineshopbackend.models.products.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class AttributeDTO {
    @NotNull
    @NotEmpty
    private final String name;

    private final String suffix;

    @NotNull
    @NotEmpty
    private final Long categoryId;

    private final LocalDateTime dateCreated;
}
