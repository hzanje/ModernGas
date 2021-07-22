package com.moderngas.pojo.admin;

import com.moderngas.pojo.CylinderTypeDto;
import lombok.Data;

import java.util.List;

@Data
public class OnboardingDto {

    private Long id;

    private Long gasId;

    private String gasName;

    private Long categoryId;

    private String categoryName;

    private String description;

    private Float price;

    private List<CylinderTypeDto> cylinderTypeList;

}
