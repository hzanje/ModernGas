package com.moderngas.service.serviceImpl;

import com.moderngas.constants.ExceptionConstants;
import com.moderngas.exception.BadRequestException;
import com.moderngas.jpaentity.*;
import com.moderngas.repository.*;
import com.moderngas.service.ValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ValidationServiceImpl implements ValidationService {

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

    @Override
    public UserEntity validateUserEntity(Long userId) throws BadRequestException {
        return userRepo.findById(userId)
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_USER_ADMIN));
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
