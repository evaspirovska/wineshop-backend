package com.systems.integrated.wineshopbackend.repository;

import com.systems.integrated.wineshopbackend.models.shopping_cart.ProductInShoppingCart;
import com.systems.integrated.wineshopbackend.models.shopping_cart.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductInShoppingCartJPARepository extends JpaRepository<ProductInShoppingCart, Long> {
    void deleteAllByShoppingCart(ShoppingCart shoppingCart);
    void deleteAllByIdAndShoppingCart(Long id, ShoppingCart shoppingCart);
}
