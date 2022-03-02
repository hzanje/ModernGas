package com.moderngas.service;

import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.admin.CylinderDto;
import com.moderngas.pojo.admin.CylinderInventoryDto;
import com.moderngas.pojo.user.InventoryDetailsDto;

import java.util.List;
import java.util.Set;

public interface InventoryService {

    String addAdminCylinder(Long adminId, List<CylinderDto> cylinderDtoList) throws BadRequestException;

    String addUserCylinder(Long adminId, List<CylinderDto> cylinderDtoList) throws BadRequestException;

    List<CylinderInventoryDto> getInventoryCylinderForAdmin(Long adminId);

    Set<InventoryDetailsDto> getUserInventory(Long id, Long adminId) throws BadRequestException;
}
