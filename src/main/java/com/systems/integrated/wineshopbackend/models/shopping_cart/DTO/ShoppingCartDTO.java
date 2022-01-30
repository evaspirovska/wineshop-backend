package com.systems.integrated.wineshopbackend.models.shopping_cart.DTO;

import com.systems.integrated.wineshopbackend.models.products.DTO.ResponseProductInSomethingDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class ShoppingCartDTO {
    private final Long id;
    private final String username;
    private final List<ResponseProductInSomethingDTO> productsInShoppingCart;
    private final LocalDateTime dateCreated;
}
