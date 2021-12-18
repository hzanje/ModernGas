package com.moderngas.pojo.user;

import com.moderngas.pojo.CylinderTypeDto;
import lombok.Data;

import java.util.List;

@Data
public class GasDto {

    private Long id;

    private String name;

    private String category;

    private boolean isRefill;

    private boolean isAvailable;

    private String description;

    private Float price;

    private List<String> imageList;

    private List<CylinderTypeDto> availableCylinderType;
}
