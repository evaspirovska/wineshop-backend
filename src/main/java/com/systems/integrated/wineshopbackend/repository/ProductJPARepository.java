package com.systems.integrated.wineshopbackend.repository;

import com.systems.integrated.wineshopbackend.models.products.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJPARepository extends JpaRepository<Product, Long> {
}
