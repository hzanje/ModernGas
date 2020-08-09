package com.moderngas.pojo;

import lombok.Data;

import java.util.List;

@Data
public class GasDto {

    private Long id;

    private String Name;

    private String cylinderType;

    private boolean isRefill;

    private boolean isAvailable;

    private List<Integer> refillRange;

    private String description;

    private Integer price;

    private List<String> imageList;
}
