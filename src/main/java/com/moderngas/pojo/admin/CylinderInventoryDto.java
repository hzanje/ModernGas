package com.moderngas.pojo.admin;

import com.moderngas.enums.CylinderStatus;
import lombok.Data;

@Data
public class CylinderInventoryDto {

    private Long id;

    private String code;

    private String status;

    private Long userId;

    private String name;

    public CylinderInventoryDto(Long id, String code, CylinderStatus cylinderStatus, Long userId, String name) {
        this.id = id;
        this.code = code;
        this.status = CylinderStatus.isExist(cylinderStatus.getName()) ? cylinderStatus.getName() : "";
        this.userId = userId;
        this.name = name;
    }
}
