package com.systems.integrated.wineshopbackend.models.products;

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
@Table(name = "Attributes")
@Data
public class Attribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    private String name;

    private String suffix;

    @ManyToOne
    @NotNull
    private Category category;

    private LocalDateTime dateCreated;
}
