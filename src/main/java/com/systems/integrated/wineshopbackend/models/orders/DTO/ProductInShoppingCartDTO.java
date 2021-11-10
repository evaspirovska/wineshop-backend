package com.systems.integrated.wineshopbackend.models.orders.DTO;

import com.systems.integrated.wineshopbackend.models.orders.ProductInShoppingCart;
import com.systems.integrated.wineshopbackend.models.products.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductInShoppingCartDTO {

    private Long productId;
//    private ProductInShoppingCart productInShoppingCart;
    private int quantity;
}
