package com.systems.integrated.wineshopbackend.models.orders.DTO;

import com.systems.integrated.wineshopbackend.models.enumerations.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateOrderStatusDTO {

    private Long orderId;
    private OrderStatus orderStatus;
}
