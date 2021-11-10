package com.systems.integrated.wineshopbackend.service.impl;

import com.systems.integrated.wineshopbackend.models.exceptions.EntityNotFoundException;
import com.systems.integrated.wineshopbackend.models.orders.DTO.ProductInOrderDTO;
import com.systems.integrated.wineshopbackend.models.orders.Order;
import com.systems.integrated.wineshopbackend.models.orders.ProductInOrder;
import com.systems.integrated.wineshopbackend.models.orders.ProductInShoppingCart;
import com.systems.integrated.wineshopbackend.models.orders.ShoppingCart;
import com.systems.integrated.wineshopbackend.models.users.User;
import com.systems.integrated.wineshopbackend.repository.*;
import com.systems.integrated.wineshopbackend.service.intef.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserJPARepository userJPARepository;
    private final OrderJPARepository orderJPARepository;
    private final ProductInOrderJPARepository productInOrderJPARepository;
    private final ShoppingCartJPARepository shoppingCartJPARepository;
    private final ProductInShoppingCartJPARepository productInShoppingCartJPARepository;

    @Override
    public List<Order> getOrders(String username) {

        User user = userJPARepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return this.orderJPARepository.findAllByUser(user);
    }

    @Override
    public Order makeOrder(String username) {

        User user = userJPARepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        ShoppingCart shoppingCart = shoppingCartJPARepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("No products in shopping cart!"));
        List<ProductInShoppingCart> productsInShoppingCart = shoppingCart.getProductsInShoppingCart();
        Order order = new Order(user);
        order.setProductsInOrder(addProductsToOrder(productsInShoppingCart, order));
        orderJPARepository.save(order);
        shoppingCart.getProductsInShoppingCart().removeAll(productsInShoppingCart);
        shoppingCartJPARepository.save(shoppingCart);
        productInShoppingCartJPARepository.deleteAll(productsInShoppingCart);
        return order;
    }

    private List<ProductInOrder> addProductsToOrder(List<ProductInShoppingCart> productsInShoppingCart, Order order) {

        List<ProductInOrder> productsInOrder = new ArrayList<>();
        for(ProductInShoppingCart p : productsInShoppingCart) {
            ProductInOrder productInOrder = ProductInOrder.builder()
                    .order(order)
                    .product(p.getProduct())
                    .quantity(p.getQuantity())
                    .dateCreated(LocalDateTime.now())
                    .build();
            productsInOrder.add(productInOrder);
            productInOrderJPARepository.save(productInOrder);
        }
        return productsInOrder;
    }
}
