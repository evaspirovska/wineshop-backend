package com.systems.integrated.wineshopbackend.models.orders.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
@Getter
public class ResponseProductInCartDTO {
    private Long id;

    private Long productId;
    @NotNull
    @NotEmpty
    private final Long categoryId;

    @NotNull
    @NotEmpty
    private final String productTitle;

    private final String productDescriptionHTML;

    @NotNull
    @NotEmpty
    private final Double priceInMKD;

    private final Map<Long, String> attributeIdAndValueMap;

    private final LocalDateTime dateCreated;

    private final int quantity;
}
