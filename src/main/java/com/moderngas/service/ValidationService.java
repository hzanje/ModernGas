package com.moderngas.service;

import com.moderngas.exception.BadRequestException;
import com.moderngas.jpaentity.*;

public interface ValidationService {

    UserEntity validateUserEntity(Long userId) throws BadRequestException;

    UserEntity validateAdminEntity(Long userId) throws BadRequestException;

    AddressEntity validateAddressEntity(Long addressId) throws BadRequestException;

    CartEntity validateCartEntity(Long cartId) throws BadRequestException;

    OrderEntity validateOrderEntity(Long orderId) throws BadRequestException;

    DeliveryVehicleEntity validateDeliveryVehicleEntity(Long vehicleId) throws BadRequestException;

    GasMaster validateGasMaster(Long gasId) throws BadRequestException;

    CylinderEntity validateInventoryById(Long cylinderId) throws BadRequestException;

    ResourceCentreEntity validateResourceCentreEntity(Long resourceCentreId) throws BadRequestException;

}
