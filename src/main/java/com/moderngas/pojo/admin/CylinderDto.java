package com.moderngas.pojo.admin;

import lombok.Data;

@Data
public class CylinderDto {

    private Long id;

    private String status;

    private Long resourceCentreId;

    private String resourceCentreName;

    private String cylinderCode;

    private String manufacturer;

    private String manufacturingDate;

    private String expiryDate;

    private String hydroTestingDate;

    private String nextHydroTestDueDate;

    private Long gasId;

    private String gasName;

    private String identifier;
}
