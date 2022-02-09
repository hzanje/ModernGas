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

    private boolean isTransit;

    private Long resourceCenterId;

    private String resourceCenterName;

    public CylinderInventoryDto(Long id, String code, CylinderStatus cylinderStatus, Long userId, String name, boolean isTransit, Long resourceCenterId, String resourceCenterName) {
        this.id = id;
        this.code = code;
        this.status = CylinderStatus.isExist(cylinderStatus.getName()) ? cylinderStatus.getName() : "";
        this.userId = userId;
        this.name = name;
        this.isTransit = isTransit;
        this.resourceCenterId = resourceCenterId;
        this.resourceCenterName = resourceCenterName;

    }
}
