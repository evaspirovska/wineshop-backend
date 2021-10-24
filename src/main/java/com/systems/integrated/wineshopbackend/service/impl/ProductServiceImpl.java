package com.systems.integrated.wineshopbackend.service.impl;

import com.systems.integrated.wineshopbackend.models.exceptions.EntityNotFoundException;
import com.systems.integrated.wineshopbackend.models.products.Attribute;
import com.systems.integrated.wineshopbackend.models.products.Category;
import com.systems.integrated.wineshopbackend.models.products.DTO.ProductDTO;
import com.systems.integrated.wineshopbackend.models.products.Product;
import com.systems.integrated.wineshopbackend.repository.CategoryJPARepository;
import com.systems.integrated.wineshopbackend.repository.ProductJPARepository;
import com.systems.integrated.wineshopbackend.service.intef.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductJPARepository productRepository;
    private final CategoryJPARepository categoryRepository;


    @Override
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + id + " not found!"));
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product create(ProductDTO productDTO) {
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + productDTO.getCategoryId() + " not found!"));
        Product product = Product.builder()
                .category(category)
                .productTitle(productDTO.getProductTitle())
                .productDescriptionHTML(productDTO.getProductDescriptionHTML())
                .priceInMKD(productDTO.getPriceInMKD())
                .pathsToProductIMGs(new LinkedList<>())
                .valueForProductAttribute(new HashMap<>())
                .build();
        //gi zemam site atributi od kategorijata i gi inicijaliziram na prazen string, korisnikot treba da stava vrednosti
        category.getAttributes().forEach(attribute -> product.getValueForProductAttribute().put(attribute, ""));
        return productRepository.save(product);
    }

    @Override
    public Product update(Long id, ProductDTO productDTO) {
        Product product = findById(id);
        product.setProductTitle(productDTO.getProductTitle());
        product.setProductDescriptionHTML(productDTO.getProductDescriptionHTML());
        product.setPriceInMKD(productDTO.getPriceInMKD());
        if (!id.equals(product.getCategory().getId())) { //ako e smeneta kategorijata na proizvodot, se brishat site atributi (zoshto se vrzani so kategorijata)
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category with id " + productDTO.getCategoryId() + " not found!"));
            product.setValueForProductAttribute(new HashMap<>());
            product.setCategory(category);
            category.getAttributes().forEach(attribute -> product.getValueForProductAttribute().put(attribute, ""));
        }
        return productRepository.save(product);
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public void updateProductAttributesForCategoryId(Long categoryId){
        //prvo gi zimame site proizvodi shto spagjaat pod promenetata kategorija
        List<Product> allProductsUnderThisCategory = productRepository.findAll().stream()
                .filter(product -> product.getCategory().getId().equals(categoryId)).collect(Collectors.toList());
        //ako nema takvi, nema shto da menuvame
        if(allProductsUnderThisCategory.size()==0)
            return;
        //ako ima, gi zemame novite atributi (seedno tuka od koj proizvod, zatoa e get(0), zoshto za site e istata kategorija
        //promeneta so novi atributi)
        List<Attribute> newAttributes = allProductsUnderThisCategory.get(0).getCategory().getAttributes();
        //za sekoj vakov proizvod, napravi nova mapa vo koja shto kje se reflektiraat novite atributi soodvetno
        allProductsUnderThisCategory.forEach(product -> {
            HashMap<Attribute, String> newMap = new HashMap<>();
            newAttributes.forEach(attribute -> newMap.put(attribute, ""));
            product.getValueForProductAttribute().forEach((key, value) -> {
                if(newAttributes.contains(key))
                    newMap.put(key, value);
            });
            product.setValueForProductAttribute(newMap);
        });
        productRepository.saveAll(allProductsUnderThisCategory);
    }
}
