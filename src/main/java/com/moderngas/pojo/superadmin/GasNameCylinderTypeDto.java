package com.moderngas.pojo.superadmin;

import lombok.Data;

import java.util.List;

@Data
public class GasNameCylinderTypeDto {

    private String name;

    private List<String> types;
}
