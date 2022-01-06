package com.moderngas.pojo.admin;

import lombok.Data;

import java.util.Date;

@Data
public class CylinderCodeStatusDto {

    private String status;

    private String cylinderCode;

    private String manufacturer;

    private Date manufacturingDate;

    private Date expiryDate;

    private Date lastService;

    private Date nextService;
}
