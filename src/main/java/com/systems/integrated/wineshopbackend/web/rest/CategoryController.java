package com.systems.integrated.wineshopbackend.web.rest;

import com.systems.integrated.wineshopbackend.models.exceptions.EntityNotFoundException;
import com.systems.integrated.wineshopbackend.models.products.Category;
import com.systems.integrated.wineshopbackend.models.products.DTO.CategoryDTO;
import com.systems.integrated.wineshopbackend.service.intef.AttributeService;
import com.systems.integrated.wineshopbackend.service.intef.CategoryService;
import com.systems.integrated.wineshopbackend.service.intef.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
@CrossOrigin(value = "*")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategory(@PathVariable Long id){
        Category category;
        try {
             category = categoryService.findById(id);
        }
        catch (EntityNotFoundException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllCategories(){
        List<Category> categories = categoryService.findAll();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNewCategory(@RequestBody CategoryDTO categoryDTO){
        Category category;
        try {
            category = categoryService.create(categoryDTO);
        }
        catch (IllegalArgumentException ex){
            return new ResponseEntity<>(ex.toString(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO){
        Category category;
        try{
            category = categoryService.update(id, categoryDTO);
        }
        catch (IllegalArgumentException ex){
            return new ResponseEntity<>(ex.toString(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id){
        categoryService.delete(id);
        return new ResponseEntity<>("Category with id " + id + " deleted.", HttpStatus.OK);
    }
}
