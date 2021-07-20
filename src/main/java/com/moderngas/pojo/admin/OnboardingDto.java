package com.moderngas.pojo.admin;

import com.moderngas.pojo.CylinderTypeDto;
import lombok.Data;

import java.util.List;

@Data
public class OnboardingDto {

    private Long id;

    private Long gasId;

    private String gasName;

    private String description;

    private Integer price;

    private List<CylinderTypeDto> cylinderTypeList;

}
