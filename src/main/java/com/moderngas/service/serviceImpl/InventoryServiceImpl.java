package com.moderngas.service.serviceImpl;

import com.moderngas.constants.Constants;
import com.moderngas.constants.ExceptionConstants;
import com.moderngas.enums.CylinderStatus;
import com.moderngas.exception.BadRequestException;
import com.moderngas.jpaentity.CylinderEntity;
import com.moderngas.jpaentity.UserEntity;
import com.moderngas.pojo.admin.CylinderCodeStatusDto;
import com.moderngas.pojo.admin.CylinderCodeStatusListDto;
import com.moderngas.pojo.admin.InventoryCylinderDto;
import com.moderngas.repository.InventoryRepo;
import com.moderngas.repository.UserRepo;
import com.moderngas.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moderngas.service.UserService;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public String addCylinder(Long userId, CylinderCodeStatusListDto cylinderCodeStatusListDto) throws BadRequestException {
        if (null == cylinderCodeStatusListDto) {
            throw new BadRequestException(ExceptionConstants.INVALID_REQUEST_DATA);
        } else if (CollectionUtils.isEmpty(cylinderCodeStatusListDto.getCylinderCodeStatusDtoList())) {
            throw new BadRequestException(ExceptionConstants.INVALID_REQUEST_DATA);
        }
        UserEntity userEntity = userRepo.findById(userId)
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_USER));
        //userService.checkIfRoleIsNotUser(userEntity);
        List<CylinderEntity> cylinderEntityList = new ArrayList<>();
        for (CylinderCodeStatusDto cylinderCodeStatusDto : cylinderCodeStatusListDto.getCylinderCodeStatusDtoList()) {
            if (!inventoryRepo.checkIfCylinderCodeExist(cylinderCodeStatusDto.getCylinderCode()).isPresent()) {
                CylinderEntity cylinderEntity = new CylinderEntity();
                CylinderStatus cylinderStatus = CylinderStatus.getByStatus(cylinderCodeStatusDto.getStatus());
                if (cylinderStatus.equals(CylinderStatus.CYLINDER_STATUS_ASSIGNED)) {
                    cylinderEntity.setUserId(userEntity.getId());
                }
                cylinderEntity.setCylinderStatus(cylinderStatus);
                cylinderEntityList.add(cylinderEntity);
            }
        }
        inventoryRepo.saveAll(cylinderEntityList);
        return Constants.SUCCESS_STR;
    }

    @Override
    public List<InventoryCylinderDto> getInventoryCylinderForAdmin(Long adminId) {
        return inventoryRepo.getInventoryCylinderForAdmin();
    }
}
