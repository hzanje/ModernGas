package com.moderngas.enums;

import com.moderngas.exception.BadRequestException;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum CylinderType {

    CYLINDER_TYPE_B("B Type", "10 Litre Cylinder in Cubic Meter (cu.m)"),
    CYLINDER_TYPE_D("D Type", "21 Litre Cylinder in Cubic Meter (cu.m)"),
    CYLINDER_TYPE_9_KG("9 Kg", "9 Kg Cylinder in Kilogram"),
    CYLINDER_TYPE_30_KG("30 Kg", "30 Kg Cylinder in Kilogram"),
    CYLINDER_TYPE_4_KG_500_GRM("4.5 Kg", "4.5 Kg Cylinder in Kilogram"),
    CYLINDER_TYPE_7_KG_500_GRM("7.5 Kg", "7.5 Kg Cylinder in Kilogram"),
    CYLINDER_TYPE_208_LITRE("208 litre", "208 Kg Cylinder in Litre Water Capacity"),
    CYLINDER_TYPE_250_LITRE("250 litre", "250 Kg Cylinder in Litre Water Capacity");

    private final String name;

    private final String description;

    CylinderType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static CylinderType getByStatus(String status) {
        if (!ObjectUtils.isEmpty(status)) {
            for (CylinderType cylinderType : CylinderType.values()) {
                if (cylinderType.getName().equals(status)) {
                    return cylinderType;
                }
            }
        }
        return null;
    }

    public static boolean isExist(String status) {
        if (!ObjectUtils.isEmpty(status)) {
            for (CylinderType cylinderType : CylinderType.values()) {
                if (cylinderType.getName().equals(status)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static CylinderType getByOrdinal(Integer ord) throws BadRequestException {
        if ((ord < 0) || (ord > CylinderType.values().length - 1)) {
            throw new BadRequestException(String.format("%d is not a valid User Type", ord));
        }
        return CylinderType.values()[ord];
    }

    public static List<CylinderType> getCylinderTypeList() {
        return new ArrayList<>(Arrays.asList(CylinderType.values()));
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
