package com.moderngas.pojo.admin;

import com.moderngas.enums.CylinderStatus;
import lombok.Data;

@Data
public class InventoryCylinderDto {

    private Long id;

    private String code;

    private String status;

    private String name;

    public InventoryCylinderDto(Long id, String code, CylinderStatus cylinderStatus, String name) {
        this.id = id;
        this.code = code;
        this.status = CylinderStatus.isExist(cylinderStatus.getName()) ? cylinderStatus.getName() : "";
        this.name = name;
    }
}
