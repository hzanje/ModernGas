package com.moderngas.service;

import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.admin.InventoryCylinderDto;
import com.moderngas.pojo.admin.ResourceCentreDto;

import java.util.List;

public interface ResourceCentreService {

    String addOrUpdateResourceCentre(List<ResourceCentreDto> resourceCentreDtoList) throws BadRequestException;

    List<ResourceCentreDto> getResourceCentre() throws BadRequestException;

    String deleteResourceCentre(Long id) throws BadRequestException;

    String addCylinderToResourceCentre(Long resourceCentreId, List<String> cylinderCodes) throws BadRequestException;

    String removeCylinderFromResourceCentre(Long resourceCentreId, List<String> cylinderCodes) throws BadRequestException;

    String addPublicCylinderToResourceCentre(Long resourceCentreId, Long userId, List<String> cylinderCodes) throws BadRequestException;

    List<InventoryCylinderDto> fetchCylinderFromResourceCentre(Long resourceCentreId, String cylinderStatus) throws BadRequestException;

    String fillCylinder(List<String> cylinderCodes) throws BadRequestException;

    String checkCylinderCode(String code) throws BadRequestException;

}
