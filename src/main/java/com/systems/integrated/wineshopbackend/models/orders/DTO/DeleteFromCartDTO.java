package com.systems.integrated.wineshopbackend.models.orders.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DeleteFromCartDTO {

    private final Long productId;

    private final String username;
}
