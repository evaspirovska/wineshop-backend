package com.systems.integrated.wineshopbackend.models.orders;

import com.systems.integrated.wineshopbackend.models.products.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "ProductsInOrders")
public class ProductInOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Order order;

    private int quantity;

    private LocalDateTime dateCreated;

//    public Double calculatePrice() {
//        return quantity * product.getPriceInMKD();
//    }
}
