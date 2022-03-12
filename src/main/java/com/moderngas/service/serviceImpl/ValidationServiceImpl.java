package com.moderngas.service.serviceImpl;

import com.moderngas.constants.ExceptionConstants;
import com.moderngas.enums.UserRole;
import com.moderngas.exception.BadRequestException;
import com.moderngas.jpaentity.*;
import com.moderngas.repository.*;
import com.moderngas.service.GenericService;
import com.moderngas.service.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class ValidationServiceImpl implements ValidationService {

    private static Logger log = LoggerFactory.getLogger(ValidationServiceImpl.class.getName());

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private DeliveryVehicleRepo deliveryVehicleRepo;

    @Autowired
    private GasRepo gasRepo;

    @Autowired
    private InventoryRepo inventoryRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ResourceCentreRepo resourceCentreRepo;

    @Autowired
    private GenericService genericService;

    /**
     * Check is user already exist in system
     * If User is added for same admin throws Error Status 422
     * If user is added for different admin update admin and save.
     *
     * @param mobileNumber
     * @param adminEntity
     * @throws BadRequestException
     */
    @Override
    public UserEntity checkUserAlreadyExistInSystem(Long mobileNumber, UserEntity adminEntity) throws BadRequestException {
        log.info("ValidationService :: checkUserAlreadyExistInSystem >>> {}", mobileNumber);
        Optional<UserEntity> userEntity = userRepo.findByMobileNumber(mobileNumber);
        if (userEntity.isPresent()) {
            if (userEntity.get().getAdminIdSet().contains(adminEntity.getId())) {
                throw new BadRequestException(ExceptionConstants.USER_ALREADY_REGISTER);
            } else {
                return userEntity.get();
            }
        }
        return new UserEntity();
    }

    @Override
    public UserEntity validateUserEntity(Long userId) throws BadRequestException {
        UserEntity userEntity = validateUser(userId);
        if (userEntity.getRoleEntitySet().stream()
                .noneMatch(e -> e.getRole().equals(UserRole.USER_ROLE_USER.getRole()))) {
            log.error("Validate User : {} >>> {}", userId, ExceptionConstants.INVALID_USER );
            throw new BadRequestException(ExceptionConstants.INVALID_USER);
        }
        return userEntity;
    }

    @Override
    public UserEntity validateAdminEntity(Long adminId) throws BadRequestException {
        UserEntity userEntity = validateUser(adminId);
        if (userEntity.getRoleEntitySet().stream()
                .noneMatch(e -> e.getRole().equals(UserRole.USER_ROLE_ADMIN.getRole())
                        || e.getRole().equals(UserRole.USER_ROLE_EMPLOYEE.getRole()))) {
            throw new BadRequestException(ExceptionConstants.INVALID_ADMIN);
        }
        return userEntity;
    }

    @Override
    public UserEntity validateSuperAdminEntity(Long superId) throws BadRequestException {
        UserEntity userEntity = validateUser(superId);
        if (userEntity.getRoleEntitySet().stream().noneMatch(e -> e.getRole().equals(UserRole.USER_ROLE_SUPER_ADMIN))) {
            throw new BadRequestException(ExceptionConstants.INVALID_SUPER_ADMIN);
        }
        return userEntity;
    }

    private UserEntity validateUser(Long id) throws BadRequestException {
        return userRepo.findById(id)
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_REGISTER_USER));
    }

    @Override
    public AddressEntity validateAddressEntity(Long addressId) throws BadRequestException {
        return addressRepo.findById(addressId)
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_USER_ADDRESS));
    }

    @Override
    public CartEntity validateCartEntity(Long cartId) throws BadRequestException {
        return cartRepo.findById(cartId)
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_CART));
    }

    @Override
    public OrderEntity validateOrderEntity(Long orderId) throws BadRequestException {
        return orderRepo.findById(orderId)
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_ORDER));
    }

    @Override
    public DeliveryVehicleEntity validateDeliveryVehicleEntity(Long vehicleId) throws BadRequestException {
        return deliveryVehicleRepo.findById(vehicleId)
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_DELIVERY_VEHICLE));
    }

    @Override
    public GasMaster validateGasMaster(Long gasId) throws BadRequestException {
        return gasRepo.findById(gasId)
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_GAS));
    }

    @Override
    public CylinderEntity validateInventoryById(Long cylinderId) throws BadRequestException {
        return inventoryRepo.findById(cylinderId)
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_CYLINDER_CODE));
    }

    @Override
    public ResourceCentreEntity validateResourceCentreEntity(Long resourceCentreId) throws BadRequestException {
        return resourceCentreRepo.findById(resourceCentreId)
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_RESOURCE_CENTRE));
    }
}
