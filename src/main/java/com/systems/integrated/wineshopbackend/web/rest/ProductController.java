package com.systems.integrated.wineshopbackend.web.rest;

import com.systems.integrated.wineshopbackend.models.exceptions.EntityNotFoundException;
import com.systems.integrated.wineshopbackend.models.products.Product;
import com.systems.integrated.wineshopbackend.models.products.DTO.ProductDTO;
import com.systems.integrated.wineshopbackend.service.intef.ImageStorageService;
import com.systems.integrated.wineshopbackend.service.intef.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
@CrossOrigin(value = "*")
public class ProductController {
    private final ProductService productService;
    private final ImageStorageService imageStorageService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id){
        Product product;
        try {
            product = productService.findById(id);
        }
        catch (EntityNotFoundException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(Product.convertToDTO(product), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts(){
        List<ProductDTO> products = productService.findAll().stream().map(Product::convertToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

//    @GetMapping
//    public ResponseEntity<?> filterProducts(@RequestParam MultiValueMap<String, String> filters){
//        //todo
//        //filters.get("filter").forEach(System.out::println);
//        //http://localhost:8080/api/products/?filter=key:value,key2:value2,key3:value3
//        return null;
//    }

    @PostMapping("/create")
    public ResponseEntity<?> createNewProduct(@RequestBody ProductDTO productDTO){
        Product product;
        try {
            product = productService.create(productDTO);
        }
        catch (IllegalArgumentException ex){
            return new ResponseEntity<>(ex.toString(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(Product.convertToDTO(product), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProduct(@RequestBody ProductDTO productDTO){
        Product product;
        try{
            product = productService.update(productDTO);
        }
        catch (IllegalArgumentException ex){
            return new ResponseEntity<>(ex.toString(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(Product.convertToDTO(product), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id){
        productService.delete(id);
        return new ResponseEntity<>("Product with id " + id + " deleted.", HttpStatus.OK);
    }

    @PutMapping("/img/{id}")
    public ResponseEntity<?> addNewProductImage(@PathVariable Long id, MultipartFile image){
        String message = "";
        String fileName = "";
        try {
            fileName = imageStorageService.saveNewImage(image, id) + ".jpg";
            message = "Image saved successfully!";
            productService.addNewProductImage(id, fileName);
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            message = "Failed to save image!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @PutMapping("/img")
    public ResponseEntity<?> setMainProductImage(@RequestParam Long productId, @RequestParam Integer mainImageId){
        productService.updateMainProductImage(productId, mainImageId + ".jpg");
        return ResponseEntity.status(HttpStatus.OK).body("success");
    }

    @PostMapping("/img")
    public ResponseEntity<?> addAllProductImages(@RequestParam Long productId, @RequestParam Integer mainImageId, @RequestParam MultipartFile[] images){
        String message = "";
        LinkedList<String> fileNames = new LinkedList<>();
        try {
            Arrays.stream(images).forEach(image -> {
                int imgNum = imageStorageService.saveNewImage(image, productId);
                fileNames.add(imgNum + ".jpg");
            });
            message = "Images saved successfully!";
            productService.updateAllProductImages(productId, fileNames);
            productService.updateMainProductImage(productId, fileNames.get(mainImageId-1));
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            message = "Failed to save images!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @DeleteMapping("/img/delete/{productId}/{imageId}")
    public ResponseEntity<?> deleteProductImage(@PathVariable Long productId, @PathVariable Integer imageId){
        productService.deleteProductImage(productId, imageId);
        return ResponseEntity.status(HttpStatus.OK).body("success");
    }

    @GetMapping("/images/{productId}")
    public ResponseEntity<List<String>> getImagePathsForProductId(@PathVariable Long productId){
        return ResponseEntity.status(HttpStatus.OK).body(imageStorageService.getNumberOfImagesForProductId(productId));
    }

    @GetMapping("/images/main/{productId}")
    public ResponseEntity<String> getMainImagePathForProductId(@PathVariable Long productId){
        return ResponseEntity.status(HttpStatus.OK).body(productService.findById(productId).getPathToMainProductIMG());
    }

    @GetMapping("/images/{productId}/{resolution}/main")
    public ResponseEntity<Resource> getMainImageForProductId(@PathVariable Long productId, @PathVariable String resolution){
        String filename = productService.findById(productId).getPathToMainProductIMG();
        Resource file = null;
        if(!filename.equals("none"))
            file = imageStorageService.load(productId + File.separator + resolution + File.separator + filename);
        else{
            file = imageStorageService.load("placeholder.png");
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("/images/{productId}/{resolution}/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String productId, @PathVariable String resolution, @PathVariable String imageName){
        Resource file = imageStorageService.load(productId + File.separator + resolution + File.separator + imageName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
