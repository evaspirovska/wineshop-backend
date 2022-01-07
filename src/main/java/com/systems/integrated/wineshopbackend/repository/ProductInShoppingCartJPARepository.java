package com.systems.integrated.wineshopbackend.repository;

import com.systems.integrated.wineshopbackend.models.orders.ProductInShoppingCart;
import com.systems.integrated.wineshopbackend.models.orders.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductInShoppingCartJPARepository extends JpaRepository<ProductInShoppingCart, Long> {
    void deleteAllByShoppingCart(ShoppingCart shoppingCart);
}
