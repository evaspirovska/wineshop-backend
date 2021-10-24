package com.systems.integrated.wineshopbackend.service.impl;

import com.systems.integrated.wineshopbackend.models.exceptions.EntityNotFoundException;
import com.systems.integrated.wineshopbackend.models.products.Category;
import com.systems.integrated.wineshopbackend.models.products.DTO.CategoryDTO;
import com.systems.integrated.wineshopbackend.repository.CategoryJPARepository;
import com.systems.integrated.wineshopbackend.service.intef.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryJPARepository categoryRepository;

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + id + " not found!"));
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category create(CategoryDTO categoryDTO) {
        Category category = Category.builder()
                .name(categoryDTO.getName())
                .attributes(new LinkedList<>())
                .products(new LinkedList<>())
                .build();
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Long id, CategoryDTO categoryDTO) {
        Category category = findById(id);
        category.setName(categoryDTO.getName());
        return categoryRepository.save(category);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
