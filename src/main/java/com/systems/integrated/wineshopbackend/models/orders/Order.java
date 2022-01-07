package com.systems.integrated.wineshopbackend.models.orders;

import com.systems.integrated.wineshopbackend.models.enumerations.OrderStatus;
import com.systems.integrated.wineshopbackend.models.users.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private User postman;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private List<ProductInOrder> productsInOrder;

    private LocalDateTime dateCreated;

    private OrderStatus orderStatus;

    private String city;

    private String telephone;

    private String address;

    public Order(User user, User postman, String city, String telephone, String address) {
        this.user = user;
        this.postman = postman;
        this.telephone = telephone;
        this.address = address;
        this.productsInOrder = new ArrayList<>();
        this.dateCreated = LocalDateTime.now();
        this.orderStatus = OrderStatus.CREATED;
        this.city = city;
    }
}
