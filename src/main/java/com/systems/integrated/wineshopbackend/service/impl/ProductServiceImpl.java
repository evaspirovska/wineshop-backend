package com.systems.integrated.wineshopbackend.service.impl;

import com.systems.integrated.wineshopbackend.models.exceptions.EntityNotFoundException;
import com.systems.integrated.wineshopbackend.models.exceptions.IllegalAttributeValueException;
import com.systems.integrated.wineshopbackend.models.products.Attribute;
import com.systems.integrated.wineshopbackend.models.products.Category;
import com.systems.integrated.wineshopbackend.models.products.DTO.ProductDTO;
import com.systems.integrated.wineshopbackend.models.products.Product;
import com.systems.integrated.wineshopbackend.repository.AttributeJPARepository;
import com.systems.integrated.wineshopbackend.repository.CategoryJPARepository;
import com.systems.integrated.wineshopbackend.repository.ProductJPARepository;
import com.systems.integrated.wineshopbackend.service.intef.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductJPARepository productRepository;
    private final CategoryJPARepository categoryRepository;
    private final AttributeJPARepository attributeRepository;


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
    public List<Product> findAllByCategoryId(Long id){
        return productRepository.findAll().stream().filter(product -> product.getCategory().getId().equals(id)).collect(Collectors.toList());
    }

    @Override
    public List<Product> filterProducts(long categoryId, double priceFrom, double priceTo, Map<Long, String> attributeIdAndValues) {
        return productRepository.findAll().stream().filter(product -> {
            boolean priceInRange = product.getPriceInMKD() >= priceFrom && product.getPriceInMKD() <= priceTo;
            AtomicBoolean attributesInRange = new AtomicBoolean(true);
            if(attributeIdAndValues != null){
                List<Long> keys = new ArrayList<>(attributeIdAndValues.keySet());
                for (long id : keys) {
                    Attribute currentAttribute = attributeRepository
                            .findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("Attribute with id " + id + " not found!"));
                    String productAttributeValue = product.getValueForProductAttribute().get(currentAttribute);
                    if (productAttributeValue == null) {
                        attributesInRange.set(false);
                        break;
                    } else if (currentAttribute.isNumeric()) {
                        double[] fromToValues = Arrays.stream(attributeIdAndValues.get(id).split("-")).mapToDouble(Double::parseDouble).toArray();
                        Arrays.sort(fromToValues);
                        if (
                                !(fromToValues[0] <= Double.parseDouble(productAttributeValue)
                                        && fromToValues[1] >= Double.parseDouble(productAttributeValue))
                        ) {
                            attributesInRange.set(false);
                            break;
                        }
                    } else {
                        attributesInRange.set(Arrays.stream(attributeIdAndValues.get(id).split("-"))
                                .anyMatch(productAttributeValue::equalsIgnoreCase));
                        if(!attributesInRange.get())
                            break;
                    }
                }
            }
            if(categoryId == -1)
                return priceInRange && attributesInRange.get();
            else
                return priceInRange && attributesInRange.get() && product.getCategory().getId().equals(categoryId);
        }).collect(Collectors.toList());
    }

    @Override
    public Product create(ProductDTO productDTO) {
        if(productDTO.getAttributeIdAndValueMap().values().stream().anyMatch(value -> value.contains("-"))){
            //pri filtriranje na proizvodi da ne se sluchi primer 200-300 shto vazhi za numerichki
            //atributi da se zbuni so tekstualna vrednost, zatoa ne dozvoluvam atribut da ima crticka vo vrednosta
            throw new IllegalArgumentException("Attribute must not have a dash \"-\" in its value!");
        }
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + productDTO.getCategoryId() + " not found!"));
        Product product = Product.builder()
                .category(category)
                .productTitle(productDTO.getProductTitle())
                .productDescriptionHTML(productDTO.getProductDescriptionHTML())
                .quantity(productDTO.getQuantity())
                .priceInMKD(productDTO.getPriceInMKD())
                .pathToMainProductIMG("none")
                .pathsToProductIMGs(new LinkedList<>())
                .valueForProductAttribute(convertAttributeIdMapToAttributeMap(productDTO.getAttributeIdAndValueMap()))
                .dateCreated(LocalDateTime.now())
                .build();
        return productRepository.save(product);
    }

    @Override
    public Product update(ProductDTO productDTO) {
        if(productDTO.getAttributeIdAndValueMap().values().stream().anyMatch(value -> value.contains("-"))){
            //pri filtriranje na proizvodi da ne se sluchi primer 200-300 shto vazhi za numerichki
            //atributi da se zbuni so tekstualna vrednost, zatoa ne dozvoluvam atribut da ima crticka vo vrednosta
            throw new IllegalArgumentException("Attribute must not have a dash \"-\" in its value!");
        }
        Product product = findById(productDTO.getId());
        product.setProductTitle(productDTO.getProductTitle());
        product.setProductDescriptionHTML(productDTO.getProductDescriptionHTML());
        product.setQuantity(productDTO.getQuantity());
        product.setPriceInMKD(productDTO.getPriceInMKD());

        if (!productDTO.getId().equals(product.getCategory().getId())) {
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category with id " + productDTO.getCategoryId() + " not found!"));
            product.setCategory(category);
            product.setValueForProductAttribute(convertAttributeIdMapToAttributeMap(productDTO.getAttributeIdAndValueMap()));
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

    private HashMap<Attribute, String> convertAttributeIdMapToAttributeMap(Map<Long, String> attributeIdAndValueMap){
        HashMap<Attribute, String> attributeAndValues = new HashMap<>();
        attributeIdAndValueMap.forEach((key, value) -> attributeAndValues.put(attributeRepository.findById(key)
                .orElseThrow(() -> new EntityNotFoundException("Attribute with id " + key + " not found!")), value));
        attributeAndValues.forEach((key, value) -> {
            if(key.isNumeric() && !isNumeric(value))
                throw new IllegalAttributeValueException(key.getName(), value);
        });
        return attributeAndValues;
    }

    private static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Override
    public void updateMainProductImage(Long id, String filePath) {
        Product product = findById(id);
        product.setPathToMainProductIMG(filePath);
        productRepository.save(product);
    }

    @Override
    public void updateAllProductImages(Long id, List<String> filePaths) {
        Product product = findById(id);
        product.setPathsToProductIMGs(filePaths);
        productRepository.save(product);
    }

    @Override
    public void deleteProductImage(Long id, Integer imageId) {
        Product product = findById(id);
        product.getPathsToProductIMGs().remove(imageId + ".jpg");
        if(product.getPathsToProductIMGs().size()==0)
            product.setPathToMainProductIMG("none");
        productRepository.save(product);
    }

    @Override
    public void addNewProductImage(Long id, String filename) {
        Product product = findById(id);
        product.getPathsToProductIMGs().add(filename);
        productRepository.save(product);
    }
}
