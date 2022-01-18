package com.moderngas.service.serviceImpl;

import com.moderngas.constants.Constants;
import com.moderngas.constants.ExceptionConstants;
import com.moderngas.enums.CylinderStatus;
import com.moderngas.exception.BadRequestException;
import com.moderngas.jpaentity.CylinderEntity;
import com.moderngas.jpaentity.CylinderInventoryDetailsEntity;
import com.moderngas.jpaentity.OrderEntity;
import com.moderngas.jpaentity.UserEntity;
import com.moderngas.pojo.employee.EmployeeSearchDto;
import com.moderngas.pojo.user.UserSearchDto;
import com.moderngas.repository.DeliveryVehicleRepo;
import com.moderngas.repository.InventoryRepo;
import com.moderngas.repository.OrderRepo;
import com.moderngas.repository.UserRepo;
import com.moderngas.service.EmployeeService;
import com.moderngas.service.ValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private DeliveryVehicleRepo deliveryVehicleRepo;

    @Autowired
    private InventoryRepo inventoryRepo;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private UserRepo userRepo;

    @Override
    public Page<EmployeeSearchDto> getAllEmployeeByAdmin(Pageable pageable, String search, Long adminId) throws BadRequestException {
        UserEntity adminEntity = validationService.validateUserEntity(adminId);
        return userRepo.getAllEmployeeByAdmin(pageable, search, adminEntity.getId());
    }

    @Override
    public String assignCylinderToUser(Long orderId, List<String> cylinderCodes) throws BadRequestException {
        OrderEntity orderEntity = validationService.validateOrderEntity(orderId);
        if (CollectionUtils.isEmpty(cylinderCodes)) {
            throw new BadRequestException(ExceptionConstants.INVALID_REQUEST_DATA);
        }
        inventoryRepo.updateCylinderToAssigned(orderEntity.getUserId(), cylinderCodes, CylinderStatus.CYLINDER_STATUS_ASSIGNED);
        return Constants.SUCCESS_STR;
    }

    @Override
    public String receiveCylinderFromUser(Long orderId, List<String> cylinderCodes) throws BadRequestException {
        OrderEntity orderEntity = validationService.validateOrderEntity(orderId);
        if (CollectionUtils.isEmpty(cylinderCodes)) {
            throw new BadRequestException(ExceptionConstants.INVALID_REQUEST_DATA);
        }
        List<CylinderEntity> cylinderEntityList = inventoryRepo.getCylinderFromCodeList(cylinderCodes);
        for (CylinderEntity cylinderEntity : cylinderEntityList) {
            cylinderEntity.setCylinderStatus(CylinderStatus.CYLINDER_STATUS_EMPTY);
            cylinderEntity.setAssignedUserId(null);
            CylinderInventoryDetailsEntity cylinderDetailsEntity = new CylinderInventoryDetailsEntity();
            if (cylinderEntity.getCylinderInventoryDetailsEntity() != null) {
                cylinderDetailsEntity = cylinderEntity.getCylinderInventoryDetailsEntity();
            }
            cylinderDetailsEntity.setTransit(true);
            cylinderDetailsEntity.setDeliveryVehicleEntity(orderEntity.getDeliveryVehicle());
            cylinderDetailsEntity.setCylinderEntity(cylinderEntity);
            cylinderEntity.setCylinderInventoryDetailsEntity(cylinderDetailsEntity);
        }
        inventoryRepo.saveAll(cylinderEntityList);
        return Constants.SUCCESS_STR;
    }

    @Override
    public List<String> getAvailableCylinder() {
        return inventoryRepo.getAvailableCylinder(CylinderStatus.CYLINDER_STATUS_FILLED);
    }

    @Override
    public List<String> getAssignedCylinderByUserId(Long userId) throws BadRequestException {
        UserEntity userEntity = validationService.validateUserEntity(userId);
        return inventoryRepo.getAssignedCylinderByUserId(userEntity.getId());
    }
}
