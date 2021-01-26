package com.moderngas.enums;

import com.moderngas.pojo.CylinderTypeDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public enum CylinderType {

    CYLINDER_TYPE_B("B", "10 Litre Cylinder"),
    CYLINDER_TYPE_D("D", "21 Litre Cylinder");

    private final String name;

    private final String description;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    CylinderType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static CylinderType getByStatus(String status) {
        if (!StringUtils.isEmpty(status)) {
            for (CylinderType cylinderType : CylinderType.values()) {
                if (cylinderType.getName().equals(status)) {
                    return cylinderType;
                }
            }
        }
        return null;
    }

    public static boolean isExist(String status) {
        if (!StringUtils.isEmpty(status)) {
            for (CylinderType cylinderType : CylinderType.values()) {
                if (cylinderType.getName().equals(status)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static CylinderType getByOrdinal(Integer ord) throws Exception {
        if((ord < 0 ) || (ord > CylinderType.values().length-1)) {
            throw new Exception(String.format("%d is not a valid User Type", ord));
        }
        return CylinderType.values()[ord];
    }

    public static List<CylinderTypeDto> getCylinderTypeDtoList() {
        List<CylinderTypeDto> cylinderTypeDtoList = new ArrayList<>();
        for (CylinderType cylinderType : CylinderType.values()) {
            cylinderTypeDtoList.add(new CylinderTypeDto(cylinderType.getName(), cylinderType.getDescription()));
        }
        return cylinderTypeDtoList;
    }

    public static List<CylinderType> getCylinderTypeList() {
        List<CylinderType> cylinderTypeList = new ArrayList<>();
        for (CylinderType cylinderType : CylinderType.values()) {
            cylinderTypeList.add(cylinderType);
        }
        return cylinderTypeList;
    }
}
