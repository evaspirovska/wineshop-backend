package com.systems.integrated.wineshopbackend.web.rest;

import com.systems.integrated.wineshopbackend.models.exceptions.EntityNotFoundException;
import com.systems.integrated.wineshopbackend.models.orders.DTO.OrderDto;
import com.systems.integrated.wineshopbackend.models.orders.DTO.ResponseOrderDTO;
import com.systems.integrated.wineshopbackend.models.orders.Order;
import com.systems.integrated.wineshopbackend.service.intef.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@CrossOrigin(value = "*")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> getOrders(@RequestParam String username) {

        List<Order> orders;
        List<ResponseOrderDTO> responseOrderDTOS;
        try {
            orders = this.orderService.getOrders(username);
            responseOrderDTOS = orders.stream().map(Order::convertToDto).collect(Collectors.toList());
        } catch (UsernameNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(responseOrderDTOS, HttpStatus.OK);
    }

    @PostMapping("/makeOrder")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> makeOrder(@RequestBody OrderDto orderDto) {

        try {
            Order order = orderService.makeOrder(orderDto);
            ResponseOrderDTO responseOrderDTO = Order.convertToDto(order);
            return new ResponseEntity<>(responseOrderDTO, HttpStatus.OK);
        } catch (UsernameNotFoundException | EntityNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
