package com.moderngas.pojo.admin;

import lombok.Data;

import java.util.List;

@Data
public class CylinderCodeStatusDto {

    private String status;

    private List<String> cylinderCodes;
}
