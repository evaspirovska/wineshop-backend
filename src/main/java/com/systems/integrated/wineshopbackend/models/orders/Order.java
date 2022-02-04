package com.systems.integrated.wineshopbackend.models.orders;

import com.systems.integrated.wineshopbackend.models.enumerations.OrderStatus;
import com.systems.integrated.wineshopbackend.models.orders.DTO.ResponseOrderDTO;
import com.systems.integrated.wineshopbackend.models.products.DTO.ProductDTO;
import com.systems.integrated.wineshopbackend.models.products.Product;
import com.systems.integrated.wineshopbackend.models.products.DTO.ResponseProductInSomethingDTO;
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

    @Enumerated(EnumType.STRING)
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

    public static ResponseOrderDTO convertToDto(Order order) {
        List<ResponseProductInSomethingDTO> responseProductsInOrder = new ArrayList<>();
        for (ProductInOrder product : order.getProductsInOrder()) {
            ProductDTO productDTO = Product.convertToDTO(product.getProduct());
            ResponseProductInSomethingDTO responseProductInSomethingDTO = new ResponseProductInSomethingDTO(
                    product.getId(),
                    productDTO.getId(),
                    productDTO.getCategoryId(),
                    productDTO.getProductTitle(),
                    productDTO.getProductDescriptionHTML(),
                    productDTO.getQuantity(),
                    productDTO.getPriceInMKD(),
                    productDTO.getAttributeIdAndValueMap(),
                    product.getDateCreated()
            );
            responseProductsInOrder.add(responseProductInSomethingDTO);
        }

        return new ResponseOrderDTO(
                order.getId(),
                order.getUser().getUsername(),
                order.getUser().getName()+" "+order.getUser().getSurname(),
                order.getPostman().getUsername(),
                order.getPostman().getName()+" "+order.getPostman().getSurname(),
                responseProductsInOrder,
                order.getDateCreated(),
                order.getOrderStatus(),
                order.getCity(),
                order.getTelephone(),
                order.getAddress()
        );
    }
}
