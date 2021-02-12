package com.moderngas.service.serviceImpl;

import com.moderngas.constants.Constants;
import com.moderngas.constants.ExceptionConstants;
import com.moderngas.enums.CylinderStatus;
import com.moderngas.exception.BadRequestException;
import com.moderngas.jpaentity.OrderEntity;
import com.moderngas.pojo.admin.CylinderCodeListDto;
import com.moderngas.repository.InventoryRepo;
import com.moderngas.repository.OrderRepo;
import com.moderngas.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private InventoryRepo inventoryRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Override
    public String assignCylinderToUser(Long orderId, CylinderCodeListDto codeListDto) throws BadRequestException {
        OrderEntity orderEntity = orderRepo.findById(orderId).orElse(null);
        if (null == orderEntity) {
            throw new BadRequestException(ExceptionConstants.INVALID_ORDER);
        }
        if (null == codeListDto || CollectionUtils.isEmpty(codeListDto.getCylinderCodes())) {
            throw new BadRequestException(ExceptionConstants.INVALID_REQUEST_DATA);
        }
        inventoryRepo.updateCylinderToAssigned(orderEntity.getUserId(), codeListDto.getCylinderCodes(), CylinderStatus.CYLINDER_STATUS_ASSIGNED);
        return Constants.SUCCESS_STR;
    }

    @Override
    public String receiveCylinderFromUser(Long orderId, CylinderCodeListDto codeListDto) throws BadRequestException {
        OrderEntity orderEntity = orderRepo.findById(orderId).orElse(null);
        if (null == orderEntity) {
            throw new BadRequestException(ExceptionConstants.INVALID_ORDER);
        }
        if (null == codeListDto || CollectionUtils.isEmpty(codeListDto.getCylinderCodes())) {
            throw new BadRequestException(ExceptionConstants.INVALID_REQUEST_DATA);
        }
        inventoryRepo.updateCylinderToEmpty(orderEntity.getUserId(), codeListDto.getCylinderCodes(), CylinderStatus.CYLINDER_STATUS_EMPTY);
        return Constants.SUCCESS_STR;
    }

    @Override
    public List<String> getAvailableCylinder() {
        return inventoryRepo.getAvailableCylinder(CylinderStatus.getByStatus("Filled"));
    }
}
