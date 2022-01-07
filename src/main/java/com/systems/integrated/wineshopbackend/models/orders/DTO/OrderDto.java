package com.systems.integrated.wineshopbackend.models.orders.DTO;

import com.systems.integrated.wineshopbackend.models.enumerations.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class OrderDto {

    private String username;

    private String city;

    private String telephone;

    private String address;
}
