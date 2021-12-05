package com.systems.integrated.wineshopbackend.models.products;

import com.systems.integrated.wineshopbackend.models.products.DTO.AttributeDTO;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Embeddable
@Data
@Table(name = "Attributes")
public class Attribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String name;

    private String suffix;

    @ManyToOne
    @NotNull
    private Category category;

    private LocalDateTime dateCreated;

    private boolean isNumeric;

    public static AttributeDTO convertToDTO(Attribute attribute){
        return new AttributeDTO(attribute.getId(), attribute.getName(), attribute.getSuffix(), attribute.getCategory().getId(), attribute.isNumeric(), attribute.getDateCreated());
    }
}
