package com.moderngas.service;

import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.admin.CylinderCodeStatusDto;
import com.moderngas.pojo.admin.InventoryCylinderDto;

import java.util.List;

public interface InventoryService {

    String addCylinder(Long userid, CylinderCodeStatusDto cylinderCodeStatusDto) throws BadRequestException;

    List<InventoryCylinderDto> getInventoryCylinderForAdmin(Long adminId);
}
