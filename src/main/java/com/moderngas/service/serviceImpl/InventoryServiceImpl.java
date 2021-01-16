package com.moderngas.service.serviceImpl;

import com.moderngas.constants.Constants;
import com.moderngas.constants.ExceptionConstants;
import com.moderngas.enums.CylinderStatus;
import com.moderngas.exception.BadRequestException;
import com.moderngas.exception.UnauthorizedException;
import com.moderngas.jpaentity.CylinderEntity;
import com.moderngas.jpaentity.UserEntity;
import com.moderngas.pojo.admin.CylinderCodeStatusDto;
import com.moderngas.repository.InventoryRepo;
import com.moderngas.repository.UserRepo;
import com.moderngas.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private InventoryRepo inventoryRepo;

    @Override
    public String addCylinder(Long userId, CylinderCodeStatusDto cylinderCodeStatusDto) {
        String response = Constants.FAILURE_STR;
        if (null == cylinderCodeStatusDto) {
            throw new BadRequestException(ExceptionConstants.INVALID_REQUEST_DATA);
        }
        UserEntity userEntity = userRepo.findById(userId).orElse(null);
        if (null == userEntity) {
            throw new BadRequestException(ExceptionConstants.INVALID_USER);
        } else if (StringUtils.isEmpty(userEntity.getRole()) || userEntity.getRole().equals("")) {
            throw new UnauthorizedException(ExceptionConstants.INVALID_USER_ACCESS);
        }
        List<CylinderEntity> cylinderEntityList = new ArrayList<>();
        for (String code : cylinderCodeStatusDto.getCylinderCodes()) {
            CylinderEntity cylinderEntity = new CylinderEntity();
            cylinderEntity.setCode(code);
            cylinderEntity.setUserId(cylinderEntity.getUserId());
            cylinderEntity.setCylinderStatus(CylinderStatus.getByStatus(cylinderCodeStatusDto.getStatus()));
            cylinderEntityList.add(cylinderEntity);
        }
        inventoryRepo.saveAll(cylinderEntityList);
        response = Constants.SUCCESS_STR;
        return response;
    }
}
