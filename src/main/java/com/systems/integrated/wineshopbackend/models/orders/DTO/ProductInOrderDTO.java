package com.systems.integrated.wineshopbackend.models.orders.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductInOrderDTO {

    private Long productId;
    private int quantity;
}
