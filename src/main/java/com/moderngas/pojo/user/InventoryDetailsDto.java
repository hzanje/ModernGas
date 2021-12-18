package com.moderngas.pojo.user;

import lombok.Data;

@Data
public class InventoryDetailsDto {

    private Long id;

    private String code;

    private Long userId;

    private String userName;

    private String location;

    public InventoryDetailsDto(Long id, String code, Long userId, String userName, String location) {
        this.id = id;
        this.code = code;
        this.userId = userId;
        this.userName = userName;
        this.location = location;
    }
}
