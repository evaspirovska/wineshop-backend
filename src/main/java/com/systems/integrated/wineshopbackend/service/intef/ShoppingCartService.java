package com.systems.integrated.wineshopbackend.service.intef;

import com.systems.integrated.wineshopbackend.models.shopping_cart.DTO.ProductInShoppingCartDTO;
import com.systems.integrated.wineshopbackend.models.shopping_cart.DTO.ShoppingCartDTO;
import com.systems.integrated.wineshopbackend.models.shopping_cart.ShoppingCart;

public interface ShoppingCartService {

    ShoppingCart getShoppingCart(String username);
    ShoppingCart addProductToShoppingCart(ProductInShoppingCartDTO productInShoppingCartDTO, String username);
    void deleteProductFromShoppingCart(Long id, String username);
    ShoppingCartDTO convertToDTO(ShoppingCart shoppingCart);
}
