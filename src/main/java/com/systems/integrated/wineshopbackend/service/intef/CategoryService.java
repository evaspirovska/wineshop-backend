package com.systems.integrated.wineshopbackend.service.intef;

import com.systems.integrated.wineshopbackend.models.products.Category;
import com.systems.integrated.wineshopbackend.models.products.DTO.CategoryDTO;

import java.util.List;

public interface CategoryService {
    Category findById(Long id);
    List<Category> findAll();
    Category create(CategoryDTO categoryDTO);
    Category update(Long id, CategoryDTO categoryDTO);
    void delete(Long id);
}
