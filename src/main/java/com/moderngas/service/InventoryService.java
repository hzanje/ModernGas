package com.moderngas.service;

import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.admin.CylinderCodeStatusListDto;
import com.moderngas.pojo.admin.InventoryCylinderDto;

import java.util.List;

public interface InventoryService {

    String addCylinder(Long userid, CylinderCodeStatusListDto cylinderCodeStatusListDto) throws BadRequestException;

    List<InventoryCylinderDto> getInventoryCylinderForAdmin(Long adminId);
}
