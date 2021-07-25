package com.moderngas.pojo.user;

import com.moderngas.pojo.CylinderTypeDto;
import lombok.Data;

import java.util.List;

@Data
public class GasDto {

    private Long id;

    private String name;

    private Long cylinderType;

    private boolean isRefill;

    private boolean isAvailable;

    private String description;

    private Integer price;

    private List<String> imageList;

    private List<CylinderTypeDto> availableCylinderType;

    private List<CylinderTypeDto> purchasedCylinderType;
}
