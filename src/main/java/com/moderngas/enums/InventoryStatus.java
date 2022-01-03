package com.moderngas.enums;

import com.moderngas.exception.BadRequestException;
import org.springframework.util.ObjectUtils;

public enum InventoryStatus {

    INVENTORY_STATUS_ASSIGNED("In"),
    INVENTORY_STATUS_FILLED("Out");

    private final String name;

    InventoryStatus(String name) {
        this.name = name;
    }

    public static InventoryStatus getByStatus(String status) {
        if (!ObjectUtils.isEmpty(status)) {
            for (InventoryStatus inventoryStatus : InventoryStatus.values()) {
                if (inventoryStatus.getName().equals(status)) {
                    return inventoryStatus;
                }
            }
        }
        return null;
    }

    public static boolean isExist(String status) {
        if (!ObjectUtils.isEmpty(status)) {
            for (InventoryStatus inventoryStatus : InventoryStatus.values()) {
                if (inventoryStatus.getName().equals(status)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static InventoryStatus getByOrdinal(Integer ord) throws BadRequestException {
        if ((ord < 0) || (ord > InventoryStatus.values().length - 1)) {
            throw new BadRequestException(String.format("%d is not a valid User Type", ord));
        }
        return InventoryStatus.values()[ord];
    }

    public String getName() {
        return name;
    }
}
