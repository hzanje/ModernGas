package com.moderngas.service.serviceImpl;

import com.moderngas.constants.Constants;
import com.moderngas.constants.ExceptionConstants;
import com.moderngas.exception.BadRequestException;
import com.moderngas.jpaentity.CylinderEntity;
import com.moderngas.jpaentity.UserEntity;
import com.moderngas.pojo.admin.CylinderDto;
import com.moderngas.pojo.admin.CylinderInventoryDto;
import com.moderngas.pojo.user.InventoryDetailsDto;
import com.moderngas.repository.InventoryRepo;
import com.moderngas.repository.UserRepo;
import com.moderngas.service.GenericService;
import com.moderngas.service.InventoryService;
import com.moderngas.service.UserService;
import com.moderngas.service.ValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private InventoryRepo inventoryRepo;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private GenericService genericService;

    @Override
    public String addCylinder(Long adminId, List<CylinderDto> cylinderDtoList) throws BadRequestException {
        if (CollectionUtils.isEmpty(cylinderDtoList)) {
            throw new BadRequestException(ExceptionConstants.INVALID_REQUEST_DATA);
        }

        UserEntity adminEntity = validationService.validateAdminEntity(adminId);
        List<CylinderEntity> cylinderEntityList = new ArrayList<>();
        for (CylinderDto cylinderDto : cylinderDtoList) {
            cylinderEntityList.add(genericService.convertDtoToCylinderEntity(cylinderDto));
        }
        Set<CylinderEntity> cylinderEntitySet = adminEntity.getCylinderEntitySet();
        cylinderEntitySet.addAll(cylinderEntityList);
        adminEntity.setCylinderEntitySet(cylinderEntitySet);
        userRepo.save(adminEntity);

        return Constants.SUCCESS_STR;
    }

    @Override
    public List<CylinderInventoryDto> getInventoryCylinderForAdmin(Long adminId) {
        return inventoryRepo.getInventoryCylinderForAdmin();
    }

    @Override
    public Set<InventoryDetailsDto> getUserInventory(Long id, Long adminId) throws BadRequestException {
        UserEntity userEntity = validationService.validateUserEntity(id);
        if (!userEntity.getAdminIdSet().contains(adminId)) {
            throw new BadRequestException(ExceptionConstants.INVALID_USER_ADMIN);
        }

        Set<InventoryDetailsDto> inventoryDetailsDtoSet = new HashSet<>();
        inventoryDetailsDtoSet.addAll(inventoryRepo.getInventoryCylinderAssignedToUser(id));
        inventoryDetailsDtoSet.addAll(inventoryRepo.getInventoryCylinderOwnedByUser(id));

        return inventoryDetailsDtoSet;
    }
}
