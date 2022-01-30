package com.systems.integrated.wineshopbackend.web.rest;

import com.systems.integrated.wineshopbackend.models.exceptions.EntityNotFoundException;
import com.systems.integrated.wineshopbackend.models.products.Attribute;
import com.systems.integrated.wineshopbackend.models.products.DTO.AttributeDTO;
import com.systems.integrated.wineshopbackend.service.intef.AttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attributes")
@CrossOrigin(value = "*")
public class AttributeController {
    private final AttributeService attributeService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getAttribute(@PathVariable Long id){
        Attribute attribute;
        try {
            attribute = attributeService.findById(id);
        }
        catch (EntityNotFoundException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(Attribute.convertToDTO(attribute), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllAttributes(){
        List<AttributeDTO> attributes = attributeService.findAll().stream().map(Attribute::convertToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(attributes, HttpStatus.OK);
    }

    @GetMapping("/bycategory/{id}")
    public ResponseEntity<?> getAttributesByCategory(@PathVariable Long id){
        List<AttributeDTO> attributes = attributeService.findAttributesByCategoryId(id).stream().map(Attribute::convertToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(attributes, HttpStatus.OK);
    }


    @PostMapping("/create")
    public ResponseEntity<?> createNewAttribute(@RequestBody AttributeDTO attributeDTO){
        Attribute attribute;
        try {
            attribute = attributeService.create(attributeDTO);
        }
        catch (IllegalArgumentException ex){
            return new ResponseEntity<>(ex.toString(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(Attribute.convertToDTO(attribute), HttpStatus.OK);
    }

    @PostMapping("/createall")
    public ResponseEntity<?> createNewAttributes(@RequestBody List<AttributeDTO> attributeDTOS){
        try {
            for(AttributeDTO attributeDTO : attributeDTOS){
                attributeService.create(attributeDTO);
            }
        }
        catch (IllegalArgumentException ex){
            return new ResponseEntity<>(ex.toString(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("All attributes created successfully!", HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateAttribute(@RequestBody AttributeDTO attributeDTO){
        Attribute attribute;
        try{
            attribute = attributeService.update(attributeDTO);
        }
        catch (IllegalArgumentException ex){
            return new ResponseEntity<>(ex.toString(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(Attribute.convertToDTO(attribute), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAttribute(@PathVariable Long id) {
        attributeService.delete(id);
        return new ResponseEntity<>("Attribute with id " + id + " deleted.", HttpStatus.OK);
    }
}
