package com.moderngas.enums;

import org.springframework.util.StringUtils;

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

    public static OrderStatus getByOrdinal(Integer ord) throws Exception {
        if((ord < 0 ) || (ord > OrderStatus.values().length-1)) {
            throw new Exception(String.format("%d is not a valid User Type", ord));
        }
        return OrderStatus.values()[ord];
    }
}
