package com.moderngas.pojo.admin;

import lombok.Data;

@Data
public class OpenOrderDto {

    private Long userId;

    private Long addressId;

    private Long productId;

    private String cylinderType;

    private int quantity;


}