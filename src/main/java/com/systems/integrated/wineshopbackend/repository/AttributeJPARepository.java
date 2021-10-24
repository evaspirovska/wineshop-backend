package com.systems.integrated.wineshopbackend.repository;

import com.systems.integrated.wineshopbackend.models.products.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttributeJPARepository extends JpaRepository<Attribute, Long> {
    List<Attribute> findAttributesByCategoryId(Long id);
}
