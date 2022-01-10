package com.systems.integrated.wineshopbackend.models.orders.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductInShoppingCartDTO {

    private final Long id;
    private final Long productId;
    private final int quantity;
}
