package com.systems.integrated.wineshopbackend.models.products;

import com.systems.integrated.wineshopbackend.models.products.DTO.CategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    @NotNull
    @NotEmpty
    private String name;
    @OneToMany(mappedBy = "category")
    private List<Attribute> attributes;
    @OneToMany(mappedBy = "category")
    private List<Product> products;

    public static CategoryDTO convertToDTO(Category category){
        return new CategoryDTO(category.getId(), category.getName());
    }
}
