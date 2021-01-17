package com.moderngas.pojo.admin;

import com.moderngas.enums.CylinderType;
import com.moderngas.enums.OrderStatus;
import lombok.Data;

import java.util.Date;

@Data
public class OrderDto {

    private Long id;

    private String cylinderType;

    private boolean isRefill;

    private Long userId;

    private String userName;

    private String gasName;

    private String categoryName;

    private String statusName;

    private int quantity;

    private Date orderDate;

    public OrderDto(Long id, CylinderType cylinderType, boolean isRefill, Long userId, String userName, String gasName, String categoryName, OrderStatus status, int quantity, Date orderDate) throws Exception {
        this.id = id;
        this.cylinderType = cylinderType.getName();
        this.isRefill = isRefill;
        this.userId = userId;
        this.userName = userName;
        this.gasName = gasName;
        this.categoryName = categoryName;
        this.statusName = status.getName();
        this.quantity = quantity;
        this.orderDate = orderDate;
    }
}
