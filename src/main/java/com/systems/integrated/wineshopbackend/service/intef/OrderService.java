package com.systems.integrated.wineshopbackend.service.intef;

import com.systems.integrated.wineshopbackend.models.orders.DTO.OrderDto;
import com.systems.integrated.wineshopbackend.models.orders.DTO.ResponseOrderDTO;
import com.systems.integrated.wineshopbackend.models.orders.DTO.UpdateOrderStatusDTO;
import com.systems.integrated.wineshopbackend.models.orders.Order;

import java.util.List;

public interface OrderService {
    List<Order> getOrders(String username);

    List<Order> getAllOrders();

    List<Order> getOrdersByPostman(String postmanUsername);

    Order makeOrder(OrderDto orderDto);

    ResponseOrderDTO convertToDto(Order order);

    Order changeOrderStatus(UpdateOrderStatusDTO updateOrderStatusDTO);
}
