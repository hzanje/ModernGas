package com.moderngas.enums;

import com.moderngas.exception.BadRequestException;
import org.springframework.util.StringUtils;

public enum CylinderStatus {

    CYLINDER_STATUS_ASSIGNED("Assigned"),
    CYLINDER_STATUS_FILLED("Filled"),
    CYLINDER_STATUS_EMPTY("Empty");

    private final String name;

    public String getName() {
        return name;
    }

    CylinderStatus(String name) {
        this.name = name;
    }

    public static CylinderStatus getByStatus(String status) {
        if (!StringUtils.isEmpty(status)) {
            for (CylinderStatus cylinderStatus : CylinderStatus.values()) {
                if (cylinderStatus.getName().equals(status)) {
                    return cylinderStatus;
                }
            }
        }
        return null;
    }

    public static boolean isExist(String status) {
        if (!StringUtils.isEmpty(status)) {
            for (CylinderStatus cylinderStatus : CylinderStatus.values()) {
                if (cylinderStatus.getName().equals(status)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static CylinderStatus getByOrdinal(Integer ord) throws BadRequestException {
        if((ord < 0 ) || (ord > CylinderStatus.values().length-1)) {
            throw new BadRequestException(String.format("%d is not a valid User Type", ord));
        }
        return CylinderStatus.values()[ord];
    }
}
