package com.systems.integrated.wineshopbackend.models.products.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
@Getter
public class ProductDTO {
    private Long id;
    @NotNull
    @NotEmpty
    private final Long categoryId;

    private final String categoryName;

    @NotNull
    @NotEmpty
    private final String productTitle;

    private final String productDescriptionHTML;

    @NotNull
    @NotEmpty
    private final Double priceInMKD;

    private final Map<Long, String> attributeIdAndValueMap;

    private final LocalDateTime dateCreated;

    public void setId(Long id){
        this.id=id;
    }
}
