package com.moderngas.pojo.user;

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

    private String category;

    private int quantity;

    private int refillCount;

    private int price;

    private String status;

    private String userName;

    private Date orderedOnDate;

    private Date loadedOnDate;

    private Date deliveredOnDate;

    private AddressDto addressDto;


}
