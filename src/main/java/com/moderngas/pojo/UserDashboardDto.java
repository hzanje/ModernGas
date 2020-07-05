package com.moderngas.pojo;

import lombok.Data;

@Data
public class UserDashboardDto {

    private Long id;

    private String name;

    private byte[] icon;

    private boolean isRefill;

    private String type;

    private int remainingRefill;
}
