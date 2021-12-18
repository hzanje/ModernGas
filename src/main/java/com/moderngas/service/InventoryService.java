package com.moderngas.service;

import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.admin.CylinderCodeStatusDto;
import com.moderngas.pojo.admin.InventoryCylinderDto;
import com.moderngas.pojo.user.InventoryDetailsDto;

import java.util.List;
import java.util.Set;

public interface InventoryService {

    String addCylinder(Long userid, List<CylinderCodeStatusDto> cylinderCodeStatusDtoList) throws BadRequestException;

    List<InventoryCylinderDto> getInventoryCylinderForAdmin(Long adminId);

    Set<InventoryDetailsDto> getUserInventory(Long id, Long adminId) throws BadRequestException;
}
