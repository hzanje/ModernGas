package com.moderngas.pojo.user;

import com.moderngas.jpaentity.CylinderTypeMaster;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class GasDto {

    private Long id;

    private String Name;

    private Long cylinderType;

    private boolean isRefill;

    private boolean isAvailable;

    private String description;

    private Integer price;

    private List<String> imageList;

    private Set<CylinderTypeMaster> availableCylinderType;

    private Set<CylinderTypeMaster> purchasedCylinderType;
}
