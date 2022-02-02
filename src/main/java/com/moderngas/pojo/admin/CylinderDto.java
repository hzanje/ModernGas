package com.moderngas.pojo.admin;

import lombok.Data;

import java.util.Date;

@Data
public class CylinderDto {

    private String status;

    private Long resourceCentreId;

    private String cylinderCode;

    private String manufacturer;

    private String manufacturingDate;

    private String expiryDate;

    private String lastService;

    private String nextService;
}
