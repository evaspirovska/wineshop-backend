package com.systems.integrated.wineshopbackend.service.intef;

import com.systems.integrated.wineshopbackend.models.products.Product;
import com.systems.integrated.wineshopbackend.models.products.DTO.ProductDTO;

import java.util.List;

public interface ProductService {
    Product findById(Long id);

    List<Product> findAll();

    Product create(ProductDTO productDTO);

    Product update(ProductDTO productDTO);

    void delete(Long id);

    void updateProductAttributesForCategoryId(Long categoryId);
}
