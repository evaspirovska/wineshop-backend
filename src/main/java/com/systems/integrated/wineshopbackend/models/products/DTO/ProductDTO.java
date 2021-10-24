package com.systems.integrated.wineshopbackend.models.products.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public class ProductDTO {
    private final Long categoryId;
    private final String productTitle;
    private final String productDescriptionHTML;
    private final Double priceInMKD;
    private final Map<Long, String> attributeIdAndValueMap;
}
