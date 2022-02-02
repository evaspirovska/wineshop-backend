package com.systems.integrated.wineshopbackend.service.intef;

import com.systems.integrated.wineshopbackend.models.products.Product;
import com.systems.integrated.wineshopbackend.models.products.DTO.ProductDTO;

import java.util.List;
import java.util.Map;

public interface ProductService {
    Product findById(Long id);

    List<Product> findAll();

    List<Product> findAllByCategoryId(Long id);

    List<Product> filterProducts(double priceFrom, double priceTo, Map<Long, String> attributeIdAndValue);

    Product create(ProductDTO productDTO);

    Product update(ProductDTO productDTO);

    void delete(Long id);

    void updateProductAttributesForCategoryId(Long categoryId);

    void updateMainProductImage(Long id, String filePath);

    void updateAllProductImages(Long id, List<String> filePaths);

    void deleteProductImage(Long id, Integer imageId);

    void addNewProductImage(Long id, String filename);
}
