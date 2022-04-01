package com.moderngas.pojo.admin;

import lombok.Data;

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

    private Long gasId;
}
