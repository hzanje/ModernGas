package com.moderngas.pojo.user;

import lombok.Data;

@Data
public class CartDto {

    private Long id;

    private String cylinderType;

    private Long userId;

    private Long adminId;

    private Long gasId;

    private String gasName;

    private String categoryName;

    private int quantity;

}
