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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ResourceCentreServiceImpl implements ResourceCentreService {

    private static Logger log = LoggerFactory.getLogger(ResourceCentreServiceImpl.class.getName());

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
                .map(r -> new ResourceCentreEntity(r.getId(), r.getName(), r.getAlias(), userEntity)).collect(Collectors.toSet());
        userEntity.setResourceCentreEntitySet(resourceCentreEntityList);
        userRepo.save(userEntity);
        return Constants.SUCCESS_STR;
    }

    @Override
    public List<ResourceCentreDto> getResourceCentre(Long adminId) throws BadRequestException {
        UserEntity userEntity = validationService.validateAdminEntity(adminId);
        return resourceCentreRepo.getResourceCentreByAdminId(userEntity.getId());
    }

    @Override
    public String deleteResourceCentre(Long id) throws BadRequestException {
        validationService.validateResourceCentreEntity(id);
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
            CylinderInventoryDetailsEntity cylinderInventoryEntity = cylinderEntity.getCylinderInventoryDetailsEntity();
            if (null == cylinderEntity.getCylinderInventoryDetailsEntity()) {
                cylinderInventoryEntity = new CylinderInventoryDetailsEntity();
            }
            cylinderInventoryEntity.setTransit(false);
            cylinderInventoryEntity.setDeliveryVehicleEntity(null);
            cylinderInventoryEntity.setResourceCentreEntity(resourceCentreEntity);
            cylinderEntity.setCylinderInventoryDetailsEntity(cylinderInventoryEntity);
            // Add only other's owner cylinder
            if (!resourceCentreEntity.getUserEntity().getId().equals(cylinderEntity.getUserEntity().getId())) {
                cylinderEntity.setAssignedUserId(resourceCentreEntity.getUserEntity().getId());
                cylinderEntity.setAssignedUserName(resourceCentreEntity.getUserEntity().getName());
            }
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
            if (resourceCentreEntity.getUserEntity().getId().equals(cylinderEntity.getAssignedUserId())) {
                cylinderInventoryEntity.setResourceCentreEntity(null);
                cylinderEntity.setAssignedUserId(null);
                cylinderEntity.setAssignedUserName(null);
            }
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
            CylinderEntity cylinderEntity = inventoryRepo.checkIfCylinderCodeExist(code, userEntity.getId()).orElse(null);
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
    public String removePublicCylinderToResourceCentre(Long resourceCentreId, Long userId, List<String> cylinderCodes) throws BadRequestException {
        ResourceCentreEntity resourceCentreEntity = validationService.validateResourceCentreEntity(resourceCentreId);
        UserEntity userEntity = validationService.validateUserEntity(userId);
        List<String> existingAnonymousCylinderCode = new ArrayList<>();
        for (String code : cylinderCodes) {
            CylinderEntity cylinderEntity = inventoryRepo.checkIfCylinderCodeExist(code, userEntity.getId()).orElse(null);
            if (null != cylinderEntity) {
                CylinderInventoryDetailsEntity cylinderInventoryEntity = new CylinderInventoryDetailsEntity();
                if (null != cylinderEntity.getCylinderInventoryDetailsEntity()) {
                    cylinderInventoryEntity = cylinderEntity.getCylinderInventoryDetailsEntity();
                }
                cylinderInventoryEntity.setTransit(true);
                cylinderEntity.setCylinderInventoryDetailsEntity(cylinderInventoryEntity);
                inventoryRepo.save(cylinderEntity);
            } else {
                existingAnonymousCylinderCode.add(code);
            }
        }
        anonymousCylinderRepo.deleteAll(anonymousCylinderRepo.getAllAnonymousCylinderById(existingAnonymousCylinderCode));
        return Constants.SUCCESS_STR;
    }

    @Override
    public Page<CylinderInventoryDto> fetchCylinderFromResourceCentre(Pageable pageable, String search, Long resourceCentreId, String cylinderStatus, Long adminId) throws BadRequestException {
        UserEntity adminEntity = validationService.validateAdminEntity(adminId);
        Set<Long> resourceCentreSet = new HashSet<>();
        ResourceCentreEntity resourceCentreEntity;
        if (null != resourceCentreId) {
            resourceCentreEntity = validationService.validateResourceCentreEntity(resourceCentreId);
            resourceCentreSet.add(resourceCentreEntity.getId());
        } else {
            resourceCentreSet.addAll(adminEntity.getResourceCentreEntitySet().stream().map(BaseEntity::getId).collect(Collectors.toSet()));
        }
        CylinderStatus cylinderStatusEnum = CylinderStatus.getByStatus(cylinderStatus);
        return inventoryRepo.fetchCylinderFromResourceCentreByIdAndStatus(pageable, search, resourceCentreSet, cylinderStatusEnum, adminEntity.getId());
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
    public String checkCylinderCode(String code, Long userId) throws BadRequestException {
        CylinderEntity cylinderEntity = inventoryRepo.checkIfCylinderCodeExist(code, userId).orElse(null);
        if (ObjectUtils.isEmpty(cylinderEntity)) {
            return Constants.SUCCESS_STR;
        }
        return Constants.FAILURE_STR;
    }
}
