package com.systems.integrated.wineshopbackend.web.rest;

import com.systems.integrated.wineshopbackend.models.exceptions.EntityNotFoundException;
import com.systems.integrated.wineshopbackend.models.exceptions.NotEnoughQuantityException;
import com.systems.integrated.wineshopbackend.models.orders.DTO.OrderDto;
import com.systems.integrated.wineshopbackend.models.orders.DTO.ResponseOrderDTO;
import com.systems.integrated.wineshopbackend.models.orders.DTO.UpdateOrderStatusDTO;
import com.systems.integrated.wineshopbackend.models.orders.Order;
import com.systems.integrated.wineshopbackend.models.products.DTO.ProductEnoughQuantityDTO;
import com.systems.integrated.wineshopbackend.models.products.Product;
import com.systems.integrated.wineshopbackend.service.intef.OrderService;
import com.systems.integrated.wineshopbackend.service.intef.ProductService;
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
    private final ProductService productService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllOrders() {
        List<ResponseOrderDTO> responseOrderDTOS;
        try {
            List<Order> orders = orderService.getAllOrders();
            responseOrderDTOS = orders.stream().map(Order::convertToDto).collect(Collectors.toList());
        } catch (UsernameNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(responseOrderDTOS, HttpStatus.OK);
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> getOrders(@PathVariable String username) {

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

    @GetMapping("/postman/{postman}")
    @PreAuthorize("hasRole('POSTMAN')")
    public ResponseEntity<?> getOrdersByPostman(@PathVariable String postman) {

        List<Order> orders;
        List<ResponseOrderDTO> responseOrderDTOS;
        try {
            orders = this.orderService.getOrdersByPostman(postman);
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
        } catch (UsernameNotFoundException | EntityNotFoundException | NotEnoughQuantityException ex) {
            if(ex instanceof NotEnoughQuantityException){
                ProductEnoughQuantityDTO peqDTO = new ProductEnoughQuantityDTO(
                        false, Product.convertToDTO(productService.findById(((NotEnoughQuantityException) ex).getProductId()))
                        );
                return new ResponseEntity<>(peqDTO, HttpStatus.OK);
            }
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update-status")
    @PreAuthorize("hasAnyRole('POSTMAN', 'ADMIN')")
    public ResponseEntity<?> updateOrderStatus(@RequestBody UpdateOrderStatusDTO updateOrderStatusDto) {
        ResponseOrderDTO responseOrderDTO;
        try {
            Order order = orderService.changeOrderStatus(updateOrderStatusDto);
            responseOrderDTO = Order.convertToDto(order);
        } catch (UsernameNotFoundException | EntityNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(responseOrderDTO, HttpStatus.OK);
    }
}
