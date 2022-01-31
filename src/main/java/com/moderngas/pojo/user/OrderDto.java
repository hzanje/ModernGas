package com.moderngas.pojo.user;

import com.moderngas.pojo.OrderDateStatusDto;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto {

    private Long id;

    private String cylinderType;

    private boolean isRefill;

    private Long userId;

    private Long adminId;

    private Long gasId;

    private String gasName;

    private String category;

    private int quantity;

    private int refillCount;

    private float price;

    private String status;

    private String userName;

    private String deliveryVehicle;

    private List<OrderDateStatusDto> orderDateStatusDto;

    private AddressDto addressDto;

    private Long addressId;


}
