package com.systems.integrated.wineshopbackend.service.impl;

import com.systems.integrated.wineshopbackend.models.exceptions.EntityNotFoundException;
import com.systems.integrated.wineshopbackend.models.products.Attribute;
import com.systems.integrated.wineshopbackend.models.products.Category;
import com.systems.integrated.wineshopbackend.models.products.DTO.AttributeDTO;
import com.systems.integrated.wineshopbackend.repository.AttributeJPARepository;
import com.systems.integrated.wineshopbackend.repository.CategoryJPARepository;
import com.systems.integrated.wineshopbackend.service.intef.AttributeService;
import com.systems.integrated.wineshopbackend.service.intef.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AttributeServiceImpl implements AttributeService {

    private final AttributeJPARepository attributeRepository;
    private final CategoryJPARepository categoryRepository;
    private final ProductService productService;

    @Override
    public Attribute findById(Long id) {
        return attributeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Attribute with id " + id + " not found!"));
    }

    @Override
    public List<Attribute> findAll() {
        return attributeRepository.findAll();
    }

    @Override
    public List<Attribute> findAttributesByCategoryId(Long id) {
        categoryRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + id + " not found!"));
        return attributeRepository.findAttributesByCategoryId(id);
    }

    @Override
    public Attribute create(AttributeDTO attributeDTO) {
        Category category = categoryRepository
                .findById(attributeDTO.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + attributeDTO.getCategoryId() + " not found!"));
        Attribute newAttribute = Attribute.builder()
                .category(category)
                .name(attributeDTO.getName())
                .suffix(attributeDTO.getSuffix())
                .build();
        Attribute savedAttribute = attributeRepository.save(newAttribute);
        productService.updateProductAttributesForCategoryId(category.getId());
        return savedAttribute;
    }

    @Override
    public Attribute update(Long id, AttributeDTO attributeDTO) {
        Category category = categoryRepository
                .findById(attributeDTO.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + attributeDTO.getCategoryId() + " not found!"));
        Attribute attribute = findById(id);
        attribute.setName(attributeDTO.getName());
        attribute.setSuffix(attributeDTO.getSuffix());
        attribute.setCategory(category);
        Attribute updatedAttribute = attributeRepository.save(attribute);
        productService.updateProductAttributesForCategoryId(category.getId());
        return updatedAttribute;
    }

    @Override
    public void delete(Long id) {
        Attribute deletedAttribute = findById(id);
        attributeRepository.deleteById(id);
        productService.updateProductAttributesForCategoryId(deletedAttribute.getCategory().getId());
    }
}