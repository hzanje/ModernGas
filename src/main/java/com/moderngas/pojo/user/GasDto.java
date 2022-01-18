package com.moderngas.pojo.user;

import com.moderngas.pojo.CylinderTypeDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    public GasDto(Long id, String name, String category, boolean isAvailable, Float price, List<CylinderTypeDto> availableCylinderType) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.isAvailable = isAvailable;
        this.price = price;
        this.availableCylinderType = availableCylinderType;
    }
}
