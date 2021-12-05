package com.systems.integrated.wineshopbackend.models.orders.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductInShoppingCartDTO {

    private Long productId;
    private int quantity;
}
