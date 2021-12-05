package com.systems.integrated.wineshopbackend.models.products;

import com.systems.integrated.wineshopbackend.models.products.DTO.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Products")
@Embeddable
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private Category category;

    @NotNull
    @NotEmpty
    private String productTitle;

    @Column(length = 4096)
    private String productDescriptionHTML;

    @NotNull
    @NotEmpty
    private Double priceInMKD;

    private String pathToMainProductIMG;

    @ElementCollection
    private List<String> pathsToProductIMGs;

    @ElementCollection
    private Map<Attribute, String> valueForProductAttribute;

    private LocalDateTime dateCreated;

    public static ProductDTO convertToDTO(Product product){
        TreeMap<Long, String> attributeValueMap = new TreeMap<>();
        product.getValueForProductAttribute().forEach((key, value) -> attributeValueMap.put(key.getId(), value));
        return new ProductDTO(product.getId(), product.getCategory().getId(),
                product.getProductTitle(), product.getProductDescriptionHTML(),
                product.getPriceInMKD(),
                attributeValueMap, product.getDateCreated());
    }
}
