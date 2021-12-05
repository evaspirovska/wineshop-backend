package com.systems.integrated.wineshopbackend.repository;

import com.systems.integrated.wineshopbackend.models.orders.ProductInOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductInOrderJPARepository extends JpaRepository<ProductInOrder, Long> {
}
