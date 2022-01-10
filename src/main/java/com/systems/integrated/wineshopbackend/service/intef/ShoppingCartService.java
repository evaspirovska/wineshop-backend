package com.systems.integrated.wineshopbackend.service.intef;

import com.systems.integrated.wineshopbackend.models.orders.DTO.ProductInShoppingCartDTO;
import com.systems.integrated.wineshopbackend.models.orders.DTO.ShoppingCartDTO;
import com.systems.integrated.wineshopbackend.models.orders.ProductInShoppingCart;
import com.systems.integrated.wineshopbackend.models.orders.ShoppingCart;

public interface ShoppingCartService {

    ShoppingCart getShoppingCart(String username);
    ShoppingCart addProductToShoppingCart(ProductInShoppingCartDTO productInShoppingCartDTO, String username);
    void deleteProductFromShoppingCart(Long id, String username);
    ShoppingCartDTO convertToDTO(ShoppingCart shoppingCart);
}
