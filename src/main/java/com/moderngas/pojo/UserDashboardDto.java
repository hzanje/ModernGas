package com.moderngas.pojo;

import lombok.Data;

import java.util.List;

@Data
public class UserDashboardDto {

    private Long id;

    private String name;

    private boolean isRefill;

    private boolean isCategory;

    private int remainingRefill;

    List<String> imageURlList;
}
