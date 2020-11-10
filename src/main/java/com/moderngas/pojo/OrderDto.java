package com.moderngas.pojo;

import lombok.Data;

@Data
public class OrderDto {

    private Long id;

    private String cylinderType;

    private boolean isRefill;

    private Long userId;

    private Long gasId;

    private String gasName;

    private String categoryName;

    private String statusName;

    private int quantity;

    private int refillCount;

    private int price;

    private String date;

    private Long statusId;
}
