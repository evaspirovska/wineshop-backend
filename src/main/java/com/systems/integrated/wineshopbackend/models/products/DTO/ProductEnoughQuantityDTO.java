package com.systems.integrated.wineshopbackend.models.products.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
@Getter
public class ProductEnoughQuantityDTO {
    boolean hasEnoughQuantity;
    ProductDTO product;
}
