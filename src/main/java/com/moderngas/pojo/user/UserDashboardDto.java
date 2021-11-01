package com.moderngas.pojo.user;

import lombok.Data;

import java.util.List;

@Data
public class UserDashboardDto {

    private Long id;

    private String name;

    List<String> imageURlList;
}
