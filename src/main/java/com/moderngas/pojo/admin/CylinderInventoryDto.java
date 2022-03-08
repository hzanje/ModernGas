package com.moderngas.pojo.admin;

import com.moderngas.enums.CylinderStatus;
import lombok.Data;

@Data
public class CylinderInventoryDto {

    private Long id;

    private String code;

    private String status;

    private Long ownerId;

    private String ownerName;

    private Long assignedId;

    private String assignedName;

    private boolean isTransit;

    private Long resourceCenterId;

    private String resourceCenterName;

    public CylinderInventoryDto(Long id, String code, CylinderStatus cylinderStatus, Long ownerId, String ownerName, Long assignedId, String assignedName, boolean isTransit, Long resourceCenterId, String resourceCenterName) {
        this.id = id;
        this.code = code;
        this.status = CylinderStatus.isExist(cylinderStatus.getName()) ? cylinderStatus.getName() : "";
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.assignedId = assignedId;
        this.assignedName = assignedName;
        this.isTransit = isTransit;
        this.resourceCenterId = resourceCenterId;
        this.resourceCenterName = resourceCenterName;

    }
}
