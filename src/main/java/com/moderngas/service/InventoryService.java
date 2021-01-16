package com.moderngas.service;

import com.moderngas.pojo.admin.CylinderCodeStatusDto;

public interface InventoryService {

    String addCylinder(Long userid, CylinderCodeStatusDto cylinderCodeStatusDto);
}
