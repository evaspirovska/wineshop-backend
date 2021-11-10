package com.systems.integrated.wineshopbackend.models.orders;

import com.systems.integrated.wineshopbackend.models.users.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "ShoppingCarts")
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    @OneToMany(mappedBy = "shoppingCart", fetch = FetchType.EAGER)
    private List<ProductInShoppingCart> productsInShoppingCart;

    private LocalDateTime dateCreated;

//    private Double totalPrice;

    public ShoppingCart(User user) {
        this.user = user;
        this.productsInShoppingCart = new ArrayList<>();
        this.dateCreated = LocalDateTime.now();
//        this.totalPrice = 0.0;
    }
}
