package com.moderngas.enums;

import com.moderngas.exception.BadRequestException;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum OrderStatus {

    ORDER_STATUS_CREATED("Ordered"),
    ORDER_STATUS_LOADED("Loaded"),
    ORDER_STATUS_DEVLIVERED("Delivered"),
    ORDER_STATUS_CANCELLED("Cancelled");

    private final String name;

    public String getName() {
        return name;
    }

    OrderStatus(String name) {
        this.name = name;
    }

    public static OrderStatus getByStatus(String status) {
        if (!StringUtils.isEmpty(status)) {
            for (OrderStatus orderStatus : OrderStatus.values()) {
                if (orderStatus.getName().equals(status)) {
                    return orderStatus;
                }
            }
        }
        return null;
    }

    public static boolean isExist(String status) {
        if (!StringUtils.isEmpty(status)) {
            for (OrderStatus orderStatus : OrderStatus.values()) {
                if (orderStatus.getName().equals(status)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static OrderStatus getByOrdinal(Integer ord) throws BadRequestException {
        if((ord < 0 ) || (ord > OrderStatus.values().length-1)) {
            throw new BadRequestException(String.format("%d is not a valid User Type", ord));
        }
        return OrderStatus.values()[ord];
    }

    public static List<OrderStatus> getOrderStatusList() {
        List<OrderStatus> orderStatusList = new ArrayList<>();
        orderStatusList.addAll(Arrays.asList(OrderStatus.values()));
        return orderStatusList;
    }
}
