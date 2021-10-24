package com.systems.integrated.wineshopbackend.models.products.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
}
