package com.moderngas.pojo;

import lombok.Data;

@Data
public class NameIdDto {

    private Long id;

    private String name;

    public NameIdDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
