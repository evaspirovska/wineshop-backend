package com.systems.integrated.wineshopbackend.models.orders.DTO;

import com.systems.integrated.wineshopbackend.models.enumerations.OrderStatus;
import com.systems.integrated.wineshopbackend.models.products.DTO.ResponseProductInSomethingDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class ResponseOrderDTO {

    private final Long id;
    private final String userUsername;
    private final String userNameAndSurname;
    private final String postmanUsername;
    private final String postmanNameAndSurname;
    private final List<ResponseProductInSomethingDTO> productsInOrder;
    private final LocalDateTime dateCreated;
    private final OrderStatus orderStatus;
    private final String city;
    private final String telephone;
    private final String address;
}
