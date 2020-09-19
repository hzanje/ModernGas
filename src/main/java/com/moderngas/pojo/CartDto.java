package com.moderngas.pojo;

import lombok.Data;

@Data
public class CartDto {

    private Long id;

    private String cylinderType;

    private Long userId;

    private Long gasId;

    private String gasName;

    private String categoryName;

        private int quantity;


}
