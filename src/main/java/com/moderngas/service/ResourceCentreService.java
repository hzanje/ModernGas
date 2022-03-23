package com.moderngas.service;

import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.admin.CylinderInventoryDto;
import com.moderngas.pojo.admin.ResourceCentreDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ResourceCentreService {

    String addOrUpdateResourceCentre(List<ResourceCentreDto> resourceCentreDtoList) throws BadRequestException;

    List<ResourceCentreDto> getResourceCentre(Long adminId) throws BadRequestException;

    String deleteResourceCentre(Long id) throws BadRequestException;

    String addCylinderToResourceCentre(Long resourceCentreId, List<String> cylinderCodes) throws BadRequestException;

    String removeCylinderFromResourceCentre(Long resourceCentreId, List<String> cylinderCodes) throws BadRequestException;

    String addPublicCylinderToResourceCentre(Long resourceCentreId, Long userId, List<String> cylinderCodes) throws BadRequestException;

    String removePublicCylinderToResourceCentre(Long resourceCentreId, Long userId, List<String> cylinderCodes) throws BadRequestException;

    Page<CylinderInventoryDto> fetchCylinderFromResourceCentre(Pageable pageable, String search, Long resourceCentreId, String cylinderStatus, Long adminId) throws BadRequestException;

    String fillCylinder(List<String> cylinderCodes) throws BadRequestException;

    String checkCylinderCode(String code, Long userId) throws BadRequestException;

}
