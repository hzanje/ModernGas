package com.moderngas.pojo.user;

import lombok.Data;

@Data
public class GasNameIdDto {

    private Long id;

    private String name;

    private String imageUrl;

    public GasNameIdDto(Long id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }
}
