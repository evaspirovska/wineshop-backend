package com.systems.integrated.wineshopbackend.repository;

import com.systems.integrated.wineshopbackend.models.products.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJPARepository extends JpaRepository<Category, Long> {
}
