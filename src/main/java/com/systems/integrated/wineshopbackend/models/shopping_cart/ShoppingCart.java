package com.systems.integrated.wineshopbackend.models.shopping_cart;

import com.systems.integrated.wineshopbackend.models.shopping_cart.DTO.ResponseProductInCartDTO;
import com.systems.integrated.wineshopbackend.models.shopping_cart.DTO.ShoppingCartDTO;
import com.systems.integrated.wineshopbackend.models.products.DTO.ProductDTO;
import com.systems.integrated.wineshopbackend.models.products.Product;
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

    public static ShoppingCartDTO convertToDTO(ShoppingCart cart) {
        List<ResponseProductInCartDTO> responseProductsInCart = new ArrayList<>();
        for (ProductInShoppingCart product : cart.getProductsInShoppingCart()) {
            ProductDTO productDTO = Product.convertToDTO(product.getProduct());
            ResponseProductInCartDTO responseProductDTO = new ResponseProductInCartDTO(
                    product.getId(),
                    productDTO.getId(),
                    productDTO.getCategoryId(),
                    productDTO.getProductTitle(),
                    productDTO.getProductDescriptionHTML(),
                    productDTO.getPriceInMKD(),
                    productDTO.getAttributeIdAndValueMap(),
                    product.getDateCreated(),
                    product.getQuantity()
            );
            responseProductsInCart.add(responseProductDTO);
        }

        return new ShoppingCartDTO(
                cart.getId(),
                cart.getUser().getUsername(),
                responseProductsInCart,
                cart.getDateCreated()
        );
    }
}
