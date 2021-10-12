package com.moderngas.service;

import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.admin.ResourceCentreDto;

import java.util.List;

public interface ResourceCentreService {

    String addOrUpdateResourceCentre(List<ResourceCentreDto> resourceCentreDtoList) throws BadRequestException;

    List<ResourceCentreDto> getResourceCentre() throws BadRequestException;

    String deleteResourceCentre(Long id) throws BadRequestException;
}
