package com.moderngas.service.serviceImpl;

import com.moderngas.constants.Constants;
import com.moderngas.constants.ExceptionConstants;
import com.moderngas.enums.CylinderStatus;
import com.moderngas.exception.BadRequestException;
import com.moderngas.jpaentity.*;
import com.moderngas.pojo.admin.CylinderInventoryDto;
import com.moderngas.pojo.admin.ResourceCentreDto;
import com.moderngas.repository.AnonymousCylinderRepo;
import com.moderngas.repository.InventoryRepo;
import com.moderngas.repository.ResourceCentreRepo;
import com.moderngas.repository.UserRepo;
import com.moderngas.service.GenericService;
import com.moderngas.service.ResourceCentreService;
import com.moderngas.service.ValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ResourceCentreServiceImpl implements ResourceCentreService {

    @Autowired
    private GenericService genericService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ResourceCentreRepo resourceCentreRepo;

    @Autowired
    private InventoryRepo inventoryRepo;

    @Autowired
    private AnonymousCylinderRepo anonymousCylinderRepo;

    @Autowired
    private ValidationService validationService;

    @Override
    public String addOrUpdateResourceCentre(List<ResourceCentreDto> resourceCentreDtoList) throws BadRequestException {
        if (CollectionUtils.isEmpty(resourceCentreDtoList)) {
            return Constants.FAILURE_STR;
        }
        UserEntity userEntity = genericService.getUserAdminDetails();
        Set<ResourceCentreEntity> resourceCentreEntityList = resourceCentreDtoList.stream()
                .map(r -> new ResourceCentreEntity(r.getId(), r.getName(), r.getAlias())).collect(Collectors.toSet());
        userEntity.setResourceCentreEntitySet(resourceCentreEntityList);
        userRepo.save(userEntity);
        return Constants.SUCCESS_STR;
    }

    @Override
    public List<ResourceCentreDto> getResourceCentre() throws BadRequestException {
        UserEntity userEntity = genericService.getUserAdminDetails();
        return resourceCentreRepo.getResourceCentreByAdminId(userEntity.getId());
    }

    @Override
    public String deleteResourceCentre(Long id) throws BadRequestException {
        resourceCentreRepo.deleteById(id);
        return Constants.SUCCESS_STR;
    }

    @Override
    public String addCylinderToResourceCentre(Long resourceCentreId, List<String> cylinderCodes) throws BadRequestException {
        ResourceCentreEntity resourceCentreEntity = validationService.validateResourceCentreEntity(resourceCentreId);
        List<CylinderEntity> cylinderEntityList = inventoryRepo.getCylinderFromCodeList(cylinderCodes);
        if (CollectionUtils.isEmpty(cylinderEntityList)) {
            return Constants.FAILURE_STR;
        }
        for (CylinderEntity cylinderEntity : cylinderEntityList) {
            CylinderInventoryDetailsEntity cylinderInventoryEntity = new CylinderInventoryDetailsEntity();
            if (null != cylinderEntity.getCylinderInventoryDetailsEntity()) {
                cylinderInventoryEntity = cylinderEntity.getCylinderInventoryDetailsEntity();
            }
            cylinderInventoryEntity.setTransit(false);
            cylinderInventoryEntity.setDeliveryVehicleEntity(null);
            cylinderInventoryEntity.setResourceCentreEntity(resourceCentreEntity);
            cylinderInventoryEntity.setCylinderEntity(cylinderEntity);
            cylinderEntity.setCylinderInventoryDetailsEntity(cylinderInventoryEntity);
        }
        inventoryRepo.saveAll(cylinderEntityList);
        return Constants.SUCCESS_STR;
    }

    @Override
    public String removeCylinderFromResourceCentre(Long resourceCentreId, List<String> cylinderCodes) throws BadRequestException {
        ResourceCentreEntity resourceCentreEntity = validationService.validateResourceCentreEntity(resourceCentreId);
        List<CylinderEntity> cylinderEntityList = inventoryRepo.getCylinderFromCodeList(cylinderCodes);
        if (CollectionUtils.isEmpty(cylinderEntityList)) {
            return Constants.FAILURE_STR;
        }
        for (CylinderEntity cylinderEntity : cylinderEntityList) {
            CylinderInventoryDetailsEntity cylinderInventoryEntity = new CylinderInventoryDetailsEntity();
            if (null != cylinderEntity.getCylinderInventoryDetailsEntity()) {
                cylinderInventoryEntity = cylinderEntity.getCylinderInventoryDetailsEntity();
            }
            cylinderInventoryEntity.setTransit(true);
            cylinderEntity.setCylinderInventoryDetailsEntity(cylinderInventoryEntity);
        }
        inventoryRepo.saveAll(cylinderEntityList);
        return Constants.SUCCESS_STR;
    }

    @Override
    public String addPublicCylinderToResourceCentre(Long resourceCentreId, Long userId, List<String> cylinderCodes) throws BadRequestException {
        ResourceCentreEntity resourceCentreEntity = validationService.validateResourceCentreEntity(resourceCentreId);
        UserEntity userEntity = validationService.validateUserEntity(userId);
        List<String> existingCylinderCode = new ArrayList<>();
        List<AnonymousCylinderEntity> anonymousCylinderEntityList = new ArrayList<>();
        for (String code : cylinderCodes) {
            CylinderEntity cylinderEntity = inventoryRepo.checkIfCylinderCodeExist(code).orElse(null);
            if (null != cylinderEntity) {
                existingCylinderCode.add(code);
            } else {
                /* Add Anonymous Cylinder to DB */
                AnonymousCylinderEntity anonymousCylinderEntity = new AnonymousCylinderEntity();
                anonymousCylinderEntity.setCode(code);
                anonymousCylinderEntity.setUserId(userEntity.getId());
                anonymousCylinderEntityList.add(anonymousCylinderEntity);
            }
        }
        if (!CollectionUtils.isEmpty(existingCylinderCode)) {
            cylinderCodes.removeAll(existingCylinderCode);
            addCylinderToResourceCentre(resourceCentreEntity.getId(), existingCylinderCode);
        }
        anonymousCylinderRepo.saveAll(anonymousCylinderEntityList);
        return Constants.SUCCESS_STR;
    }

    @Override
    public List<CylinderInventoryDto> fetchCylinderFromResourceCentre(Long resourceCentreId, String cylinderStatus) throws BadRequestException {
        ResourceCentreEntity resourceCentreEntity = validationService.validateResourceCentreEntity(resourceCentreId);
        List<CylinderInventoryDto> cylinderCodeIdList;
        if (ObjectUtils.isEmpty(cylinderStatus)) {
            cylinderCodeIdList = inventoryRepo.fetchCylinderFromResourceCentreById(resourceCentreEntity.getId());
        } else {
            CylinderStatus cylinderStatusEnum = CylinderStatus.getByStatus(cylinderStatus);
            cylinderCodeIdList = inventoryRepo.fetchCylinderFromResourceCentreByIdAndStatus(resourceCentreEntity.getId(), cylinderStatusEnum);
        }
        return cylinderCodeIdList;
    }

    @Override
    public String fillCylinder(List<String> cylinderCodes) throws BadRequestException {
        List<CylinderEntity> cylinderEntityList = inventoryRepo.getCylinderFromCodeList(cylinderCodes);
        if (CollectionUtils.isEmpty(cylinderEntityList)) {
            throw new BadRequestException(ExceptionConstants.INVALID_CYLINDER_CODE);
        }
        cylinderEntityList.forEach(c -> c.setCylinderStatus(CylinderStatus.CYLINDER_STATUS_FILLED));
        inventoryRepo.saveAll(cylinderEntityList);
        return Constants.SUCCESS_STR;
    }

    @Override
    public String checkCylinderCode(String code) throws BadRequestException {
        CylinderEntity cylinderEntity = inventoryRepo.checkIfCylinderCodeExist(code).orElse(null);
        if (ObjectUtils.isEmpty(cylinderEntity)) {
            return Constants.SUCCESS_STR;
        }
        return Constants.FAILURE_STR;
    }
}
