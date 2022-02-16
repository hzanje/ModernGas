package com.moderngas.service.serviceImpl;

import com.moderngas.constants.Constants;
import com.moderngas.constants.ExceptionConstants;
import com.moderngas.enums.CylinderStatus;
import com.moderngas.enums.UserRole;
import com.moderngas.exception.BadRequestException;
import com.moderngas.jpaentity.*;
import com.moderngas.pojo.UserDto;
import com.moderngas.pojo.admin.CylinderInventoryDto;
import com.moderngas.pojo.employee.EmployeeEntityResponseDto;
import com.moderngas.pojo.user.UserSearchDto;
import com.moderngas.repository.DeliveryVehicleRepo;
import com.moderngas.repository.InventoryRepo;
import com.moderngas.repository.UserRepo;
import com.moderngas.service.EmployeeService;
import com.moderngas.service.GenericService;
import com.moderngas.service.ValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.security.NoSuchAlgorithmException;
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
    private GenericService genericService;

    @Autowired
    private UserRepo userRepo;

    @Override
    public String addEmployee(Long adminId, UserDto userDto) throws BadRequestException, NoSuchAlgorithmException {
        UserEntity adminEntity = validationService.validateAdminEntity(adminId);
        validationService.checkUserAlreadyExistInSystem(userDto.getMobileNumber());
        UserEntity employeeEntity = new UserEntity();
        if (!ObjectUtils.isEmpty(userDto.getId())) {
            employeeEntity = validationService.validateAdminEntity(userDto.getId());
        }
        employeeEntity = genericService.convertUserDtoToEntity(employeeEntity, userDto, adminEntity, UserRole.USER_ROLE_EMPLOYEE);
        employeeEntity.setPassword(genericService.encodeUserPassword(genericService.generateRandomPassword()));
        userRepo.save(employeeEntity);
        return Constants.SUCCESS_STR;
    }

    @Override
    public String updateEmployee(Long adminId, UserDto userDto) throws BadRequestException {
        UserEntity adminEntity = validationService.validateAdminEntity(adminId);
        UserEntity employeeEntity = validationService.validateAdminEntity(userDto.getId());
        employeeEntity = genericService.convertUserDtoToEntity(employeeEntity, userDto, adminEntity, UserRole.USER_ROLE_EMPLOYEE);
        employeeEntity.setCompanyName(userDto.getCompanyName());
        if (null != userDto.getPassword() && !userDto.getPassword().isEmpty()) {
            employeeEntity.setPassword(genericService.encodeUserPassword(userDto.getPassword()));
        }
        userRepo.save(employeeEntity);
        return Constants.SUCCESS_STR;
    }

    @Override
    public Page<UserSearchDto> getAllEmployeeByAdmin(Pageable pageable, String search, Long adminId) throws BadRequestException {
        UserEntity adminEntity = validationService.validateAdminEntity(adminId);
        return userRepo.getAllUserByAdmin(pageable, search, adminEntity.getId(), UserRole.USER_ROLE_EMPLOYEE.getRole());
    }

    @Override
    public EmployeeEntityResponseDto getEmployeeById(Long employeeId) throws BadRequestException {
        UserEntity employeeEntity = validationService.validateAdminEntity(employeeId);
        EmployeeEntityResponseDto employeeEntityResponseDto = new EmployeeEntityResponseDto();
        employeeEntityResponseDto.setId(employeeEntity.getId());
        employeeEntityResponseDto.setName(employeeEntity.getName());
        employeeEntityResponseDto.setMobileNumber(employeeEntity.getMobileNumber());
        employeeEntityResponseDto.setEmail(employeeEntity.getEmail());
        employeeEntityResponseDto.setCompany(employeeEntity.getCompanyName());
        if (!CollectionUtils.isEmpty(employeeEntity.getRoleEntitySet())) {
            UserRoleEntity employeeRole = employeeEntity.getRoleEntitySet().stream()
                    .filter(e -> e.getRole().equals(UserRole.USER_ROLE_EMPLOYEE.getRole()))
                    .findFirst()
                    .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_EMPLOYEE));
            employeeEntityResponseDto.setPrivilegeDtoSet(genericService.convertToPrivilegeDto(employeeRole.getUserPrivilegeSet()));
        }
        return employeeEntityResponseDto;
    }

    @Override
    public String assignCylinderToUser(Long orderId, List<String> cylinderCodes) throws BadRequestException {
        OrderEntity orderEntity = validationService.validateOrderEntity(orderId);
        UserEntity userEntity = validationService.validateUserEntity(orderEntity.getUserId());
        if (CollectionUtils.isEmpty(cylinderCodes)) {
            throw new BadRequestException(ExceptionConstants.INVALID_REQUEST_DATA);
        }
        inventoryRepo.updateCylinderToAssigned(userEntity.getId(), userEntity.getName(), cylinderCodes, CylinderStatus.CYLINDER_STATUS_ASSIGNED);
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
            cylinderEntity.setAssignedUserName(null);
            CylinderInventoryDetailsEntity cylinderDetailsEntity = new CylinderInventoryDetailsEntity();
            if (cylinderEntity.getCylinderInventoryDetailsEntity() != null) {
                cylinderDetailsEntity = cylinderEntity.getCylinderInventoryDetailsEntity();
            }
            cylinderDetailsEntity.setTransit(true);
            cylinderDetailsEntity.setDeliveryVehicleEntity(orderEntity.getDeliveryVehicle());
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
    public List<CylinderInventoryDto> getAssignedCylinderByUserId(Long userId) throws BadRequestException {
        UserEntity userEntity = validationService.validateUserEntity(userId);
        return inventoryRepo.getAssignedCylinderByUserId(userEntity.getId());
    }
}
