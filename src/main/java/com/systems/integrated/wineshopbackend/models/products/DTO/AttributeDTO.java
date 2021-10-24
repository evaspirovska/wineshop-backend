package com.systems.integrated.wineshopbackend.models.products.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AttributeDTO {
    private final String name;
    private final String suffix;
    private final Long categoryId;
}
