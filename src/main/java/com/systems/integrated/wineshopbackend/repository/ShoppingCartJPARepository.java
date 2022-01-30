package com.systems.integrated.wineshopbackend.repository;

import com.systems.integrated.wineshopbackend.models.shopping_cart.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartJPARepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByUser_Id(Long id);
}
