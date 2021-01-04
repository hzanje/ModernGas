package com.moderngas.pojo.admin;

import lombok.Data;

@Data
public class OrderDto {

    private Long id;

    private String cylinderType;

    private boolean isRefill;

    private Long userId;

    private String userName;

    private String gasName;

    private String categoryName;

    private Long statusId;

    private String statusName;

    private int quantity;

    public OrderDto(Long id, String cylinderType, boolean isRefill, Long userId, String userName, String gasName, String categoryName, Long statusId, String statusName, int quantity) {
        this.id = id;
        this.cylinderType = cylinderType;
        this.isRefill = isRefill;
        this.userId = userId;
        this.userName = userName;
        this.gasName = gasName;
        this.categoryName = categoryName;
        this.statusId = statusId;
        this.statusName = statusName;
        this.quantity = quantity;
    }
}
