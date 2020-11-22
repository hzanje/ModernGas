package com.moderngas.pojo;

import lombok.Data;

import java.util.Date;

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

    private Long statusId;

    private Date orderedOnDate;

    private Date loadedOnDate;

    private Date shippedOnDate;

    private Date deliveredOnDate;

    private AddressDto addressDto;
}
