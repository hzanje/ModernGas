package com.moderngas.service;

import com.moderngas.enums.OrderStatus;
import com.moderngas.exception.BadRequestException;
import com.moderngas.jpaentity.*;
import com.moderngas.pojo.admin.DeliveryVehicleDto;
import com.moderngas.pojo.admin.FilterDto;
import com.moderngas.pojo.admin.OnboardingDto;
import com.moderngas.pojo.superadmin.AdminEntityDto;
import com.moderngas.pojo.user.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;

public interface GenericService {

    UserEntity convertDtoToUserData(UserEntityDto userEntityDto) throws BadRequestException;

    UserEntity convertDtoToUserData(AdminEntityDto adminEntityDto) throws BadRequestException;

    UserEntityDto convertUserDataToDto(UserEntity clientEntity) throws BadRequestException;

    String encodeUserPassword(String password);

    String generateRandomPassword() throws NoSuchAlgorithmException;

    AddressEntity convertDtoToAddressEntity(AddressDto addressDto, AddressEntity addressEntity);

    Set<AddressDto> convertAddressEntitySetToDto(Set<AddressEntity> addressEntity);

    AddressDto convertAddressEntityToDto(AddressEntity addressEntity);

    Set<UserDashboardDto> convertCategoryToDto(List<CategoryMaster> categoryMasterList, Set<UserDashboardDto> hashSet);

    OrderEntity convertDtoToOrderEntity(OrderDto orderDto) throws BadRequestException;

    OrderDto convertOrderEntityToDto(OrderEntity orderEntity);

    CartEntity convertDtoToCartEntity(CartDto cartDto) throws BadRequestException;

    CartDto convertCartEntityToDto(CartEntity cartEntity);

    List<OrderEntity> convertCartToOrderEntity(List<CartEntity> cartEntityList, Long addressId) throws BadRequestException;

    OrderEntity changeOrderStatus(OrderEntity orderEntity, OrderStatus orderStatus, Long deliveryVehicleId);

    FilterDto getFilterList();

    List<DeliveryVehicleEntity> convertDtoToDeliveryVehicle(DeliveryVehicleDto deliveryVehicleDto);

    List<OnboardingDto> convertUserDateToOnboardingList(UserEntity userEntity) throws BadRequestException;

    UserEntity getUserAdminDetails() throws BadRequestException;

    UserEntity getUserAndCheckUserAdmin(Long userId, Long adminId) throws BadRequestException;

    Set<UserDashboardDto> convertGasMappingToDashboardDto(List<AdminGasMapping> adminGasMappingList);
}
