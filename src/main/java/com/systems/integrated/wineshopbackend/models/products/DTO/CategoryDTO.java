package com.systems.integrated.wineshopbackend.models.products.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class CategoryDTO {
    private Long id;
    @NotNull
    @NotEmpty
    private final String name;

    public void setId(Long id){
        this.id=id;
    }
}
