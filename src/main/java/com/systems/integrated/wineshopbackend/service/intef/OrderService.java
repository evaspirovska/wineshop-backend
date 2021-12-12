package com.systems.integrated.wineshopbackend.service.intef;

//import com.systems.integrated.wineshopbackend.models.orders.DTO.ProductInOrderDTO;
import com.systems.integrated.wineshopbackend.models.orders.Order;
import com.systems.integrated.wineshopbackend.models.orders.ProductInShoppingCart;

import java.util.List;

public interface OrderService {
    List<Order> getOrders(String username);
    Order makeOrder(String username);
}
