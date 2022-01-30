package com.systems.integrated.wineshopbackend.service.impl;

import com.systems.integrated.wineshopbackend.models.enumerations.OrderStatus;
import com.systems.integrated.wineshopbackend.models.exceptions.EntityNotFoundException;
import com.systems.integrated.wineshopbackend.models.orders.DTO.OrderDto;
import com.systems.integrated.wineshopbackend.models.orders.DTO.ResponseOrderDTO;
import com.systems.integrated.wineshopbackend.models.orders.Order;
import com.systems.integrated.wineshopbackend.models.orders.ProductInOrder;
import com.systems.integrated.wineshopbackend.models.shopping_cart.ShoppingCart;
import com.systems.integrated.wineshopbackend.models.users.Postman;
import com.systems.integrated.wineshopbackend.models.users.User;
import com.systems.integrated.wineshopbackend.repository.*;
import com.systems.integrated.wineshopbackend.service.intef.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final UserJPARepository userJPARepository;
    private final OrderJPARepository orderJPARepository;
    private final ProductInOrderJPARepository productInOrderJPARepository;
    private final ShoppingCartJPARepository shoppingCartJPARepository;
    private final ProductInShoppingCartJPARepository productInShoppingCartJPARepository;
    private final PostmanJPARepository postmanJPARepository;

    @Override
    public List<Order> getOrders(String username) {

        User user = userJPARepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return this.orderJPARepository.findAllByUser(user);
    }

    @Override
    public Order makeOrder(OrderDto orderDto) {

        User user = userJPARepository.findUserByUsername(orderDto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(orderDto.getUsername()));
        ShoppingCart shoppingCart = shoppingCartJPARepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("No products in shopping cart!"));
        Order order = createOrder(orderDto, user);
        order.setOrderStatus(OrderStatus.CREATED);
        orderJPARepository.save(order);
        order.setProductsInOrder(addProductsToOrder(order, shoppingCart));
        orderJPARepository.save(order);
        return order;
    }

    @Override
    public ResponseOrderDTO convertToDto(Order order) {
        return Order.convertToDto(order);
    }

    private Order createOrder(OrderDto orderDto, User user) {

        List<Postman> postmanList = this.postmanJPARepository.findAllByCity(orderDto.getCity())
                .orElseThrow(() -> new EntityNotFoundException("No postman in city " + orderDto.getCity()));
        Postman postman = postmanList.stream().min(Comparator.comparing(Postman::getOrdersToDeliver)).get();
        postman.updateCount();
        this.postmanJPARepository.save(postman);
        Order order =
                new Order(user, postman.getUser(), orderDto.getCity(), orderDto.getTelephone(), orderDto.getAddress());
        return order;
    }

    private List<ProductInOrder> addProductsToOrder(Order order, ShoppingCart shoppingCart) {

        List<ProductInOrder> productsInOrder = new ArrayList<>();
        shoppingCart.getProductsInShoppingCart().forEach(p -> productsInOrder.add(ProductInOrder.builder()
                .order(order)
                .product(p.getProduct())
                .quantity(p.getQuantity())
                .dateCreated(LocalDateTime.now())
                .build()));
        this.productInOrderJPARepository.saveAll(productsInOrder);
//        shoppingCart.getProductsInShoppingCart().removeAll(productsInShoppingCart);
        this.productInShoppingCartJPARepository.deleteAllByShoppingCart(shoppingCart);
        this.shoppingCartJPARepository.save(shoppingCart);
        return productsInOrder;
    }
}
