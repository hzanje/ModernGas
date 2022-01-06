package com.moderngas.service.serviceImpl;

import com.moderngas.constants.Constants;
import com.moderngas.constants.ExceptionConstants;
import com.moderngas.enums.CylinderStatus;
import com.moderngas.exception.BadRequestException;
import com.moderngas.jpaentity.CylinderEntity;
import com.moderngas.jpaentity.UserEntity;
import com.moderngas.pojo.admin.CylinderCodeStatusDto;
import com.moderngas.pojo.admin.InventoryCylinderDto;
import com.moderngas.pojo.user.InventoryDetailsDto;
import com.moderngas.repository.InventoryRepo;
import com.moderngas.repository.UserRepo;
import com.moderngas.service.InventoryService;
import com.moderngas.service.UserService;
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

    @Override
    public String addCylinder(Long adminId, List<CylinderCodeStatusDto> cylinderCodeStatusDtoList) throws BadRequestException {
        if (CollectionUtils.isEmpty(cylinderCodeStatusDtoList)) {
            throw new BadRequestException(ExceptionConstants.INVALID_REQUEST_DATA);
        }

        UserEntity userEntity = userRepo.findById(adminId)
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_USER));
        List<CylinderEntity> cylinderEntityList = new ArrayList<>();
        for (CylinderCodeStatusDto cylinderCodeStatusDto : cylinderCodeStatusDtoList) {
            if (!inventoryRepo.checkIfCylinderCodeExist(cylinderCodeStatusDto.getCylinderCode()).isPresent()) {
                CylinderEntity cylinderEntity = new CylinderEntity();
                CylinderStatus cylinderStatus = CylinderStatus.getByStatus(cylinderCodeStatusDto.getStatus());
                cylinderEntity.setCode(cylinderCodeStatusDto.getCylinderCode());
                cylinderEntity.setCylinderStatus(cylinderStatus);
                cylinderEntity.setManufacturer(cylinderCodeStatusDto.getManufacturer());
                cylinderEntity.setManufacturingDate(cylinderCodeStatusDto.getManufacturingDate());
                cylinderEntity.setExpiryDate(cylinderCodeStatusDto.getExpiryDate());
                cylinderEntity.setLastService(cylinderCodeStatusDto.getLastService());
                cylinderEntity.setNextService(cylinderCodeStatusDto.getNextService());
                cylinderEntityList.add(cylinderEntity);
            }
        }
        List<CylinderEntity> savedCylinder = inventoryRepo.saveAll(cylinderEntityList);
        Set<CylinderEntity> cylinderEntitySet = userEntity.getCylinderEntitySet();
        cylinderEntitySet.addAll(savedCylinder);
        userEntity.setCylinderEntitySet(cylinderEntitySet);
        userRepo.save(userEntity);

        return Constants.SUCCESS_STR;
    }

    @Override
    public List<InventoryCylinderDto> getInventoryCylinderForAdmin(Long adminId) {
        return inventoryRepo.getInventoryCylinderForAdmin();
    }

    @Override
    public Set<InventoryDetailsDto> getUserInventory(Long id, Long adminId) throws BadRequestException {
        UserEntity userEntity = userRepo.findById(id)
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_USER));
        if (!userEntity.getAdminIdSet().contains(adminId)) {
            throw new BadRequestException(ExceptionConstants.INVALID_USER_ADMIN);
        }

        Set<InventoryDetailsDto> inventoryDetailsDtoSet = new HashSet<>();
        inventoryDetailsDtoSet.addAll(inventoryRepo.getInventoryCylinderAssignedToUser(id));
        inventoryDetailsDtoSet.addAll(inventoryRepo.getInventoryCylinderOwnedByUser(id));

        return inventoryDetailsDtoSet;
    }
}
