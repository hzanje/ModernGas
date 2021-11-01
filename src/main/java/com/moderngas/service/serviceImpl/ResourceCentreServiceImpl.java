package com.moderngas.service.serviceImpl;

import com.moderngas.constants.Constants;
import com.moderngas.constants.ExceptionConstants;
import com.moderngas.enums.CylinderStatus;
import com.moderngas.exception.BadRequestException;
import com.moderngas.jpaentity.CylinderEntity;
import com.moderngas.jpaentity.CylinderInventoryDetailsEntity;
import com.moderngas.jpaentity.ResourceCentreEntity;
import com.moderngas.jpaentity.UserEntity;
import com.moderngas.pojo.NameIdDto;
import com.moderngas.pojo.admin.ResourceCentreDto;
import com.moderngas.repository.InventoryRepo;
import com.moderngas.repository.ResourceCentreRepo;
import com.moderngas.repository.UserRepo;
import com.moderngas.service.GenericService;
import com.moderngas.service.ResourceCentreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
        UserEntity userEntity = genericService.getUserAdminDetails();
        ResourceCentreEntity resourceCentreEntity = resourceCentreRepo.findById(resourceCentreId)
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_RESOURCE_CENTRE));
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
            cylinderInventoryEntity.setResourceCentreEntity(resourceCentreEntity);
            cylinderInventoryEntity.setCylinderEntity(cylinderEntity);
            cylinderEntity.setCylinderInventoryDetailsEntity(cylinderInventoryEntity);
        }
        inventoryRepo.saveAll(cylinderEntityList);
        return Constants.SUCCESS_STR;
    }

    @Override
    public List<NameIdDto> fetchCylinderFromResourceCentre(Long resourceCentreId, String cylinderStatus) throws BadRequestException {
        ResourceCentreEntity resourceCentreEntity = resourceCentreRepo.findById(resourceCentreId)
                .orElseThrow(() -> new BeanCreationException(ExceptionConstants.INVALID_RESOURCE_CENTRE));
        List<NameIdDto> cylinderCodeIdList;
        if (StringUtils.isEmpty(cylinderStatus)) {
            cylinderCodeIdList = inventoryRepo.fetchCylinderFromResourceCentreById(resourceCentreEntity.getId());
        } else {
            CylinderStatus cylinderStatusEnum = CylinderStatus.getByStatus(cylinderStatus);
            cylinderCodeIdList = inventoryRepo.fetchCylinderFromResourceCentreByIdAndStatus(resourceCentreEntity.getId(), cylinderStatusEnum);
        }
        return cylinderCodeIdList;
    }
}
