package com.moderngas.service;

import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.admin.CylinderDto;
import com.moderngas.pojo.admin.CylinderInventoryDto;

import java.util.List;
import java.util.Set;

public interface InventoryService {

    String addAdminCylinder(Long adminId, List<CylinderDto> cylinderDtoList) throws BadRequestException;

    String addUserCylinder(Long adminId, List<CylinderDto> cylinderDtoList) throws BadRequestException;

    String updateAdminCylinder(Long adminId, CylinderDto cylinderDto) throws BadRequestException;

    String updateUserCylinder(Long userId, CylinderDto cylinderDto) throws BadRequestException;

    List<CylinderInventoryDto> getInventoryCylinderForAdmin(Long adminId);

    Set<CylinderInventoryDto> getUserInventory(Long id, Long adminId) throws BadRequestException;

    CylinderDto getCylinderDetailsByCode(String code) throws BadRequestException;

    String decryptCylinderEntity(String encryptedCode) throws BadRequestException;
}
