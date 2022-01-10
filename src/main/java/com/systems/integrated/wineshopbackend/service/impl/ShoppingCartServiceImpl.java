package com.systems.integrated.wineshopbackend.service.impl;

import com.systems.integrated.wineshopbackend.models.exceptions.EntityNotFoundException;
import com.systems.integrated.wineshopbackend.models.orders.DTO.ProductInShoppingCartDTO;
import com.systems.integrated.wineshopbackend.models.orders.DTO.ShoppingCartDTO;
import com.systems.integrated.wineshopbackend.models.orders.ProductInShoppingCart;
import com.systems.integrated.wineshopbackend.models.orders.ShoppingCart;
import com.systems.integrated.wineshopbackend.models.products.Product;
import com.systems.integrated.wineshopbackend.models.users.User;
import com.systems.integrated.wineshopbackend.repository.ProductInShoppingCartJPARepository;
import com.systems.integrated.wineshopbackend.repository.ProductJPARepository;
import com.systems.integrated.wineshopbackend.repository.ShoppingCartJPARepository;
import com.systems.integrated.wineshopbackend.repository.UserJPARepository;
import com.systems.integrated.wineshopbackend.service.intef.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartJPARepository shoppingCartJPARepository;
    private final ProductInShoppingCartJPARepository productInShoppingCartJPARepository;
    private final UserJPARepository userJPARepository;
    private final ProductJPARepository productJPARepository;

    @Override
    public ShoppingCart getShoppingCart(String username) {

        User user = userJPARepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        ShoppingCart shoppingCart = shoppingCartJPARepository.findByUser_Id(user.getId()).orElse(new ShoppingCart(user));
        shoppingCartJPARepository.save(shoppingCart);
        return shoppingCart;
    }

    @Override
    public ShoppingCart addProductToShoppingCart(ProductInShoppingCartDTO productInShoppingCartDTO, String username) {

        User user = userJPARepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return this.build(productInShoppingCartDTO, user).getShoppingCart();
    }

    @Override
    public void deleteProductFromShoppingCart(Long id, String username) {

        ShoppingCart shoppingCart = this.getShoppingCart(username);
        ProductInShoppingCart productInShoppingCart = productInShoppingCartJPARepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("product in shopping cart with id: " + id.toString() + "not found!"));
        productInShoppingCartJPARepository.deleteAllByIdAndShoppingCart(id, shoppingCart);
    }

    @Override
    public ShoppingCartDTO convertToDTO(ShoppingCart shoppingCart) {
        return ShoppingCart.convertToDTO(shoppingCart);
    }

    private ProductInShoppingCart build(ProductInShoppingCartDTO productInShoppingCartDTO, User user) {

        Product product = productJPARepository.findById(productInShoppingCartDTO.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + productInShoppingCartDTO.getProductId() + " not found!"));
        ProductInShoppingCart productInShoppingCart = ProductInShoppingCart.builder()
                .shoppingCart(this.getShoppingCart(user.getUsername()))
                .product(product)
                .quantity(productInShoppingCartDTO.getQuantity())
                .dateCreated(LocalDateTime.now())
                .build();
        productInShoppingCartJPARepository.save(productInShoppingCart);
        return productInShoppingCart;
    }

}
