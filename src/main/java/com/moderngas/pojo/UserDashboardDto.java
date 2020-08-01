package com.moderngas.pojo;

import lombok.Data;

import java.util.List;

@Data
public class UserDashboardDto {

    private Long id;

    private String name;

    private boolean isCategory;

    List<String> imageURlList;
}
