package com.moderngas.service;

import com.moderngas.enums.OrderStatus;
import com.moderngas.exception.BadRequestException;
import com.moderngas.jpaentity.AddressEntity;
import com.moderngas.jpaentity.CartEntity;
import com.moderngas.jpaentity.CategoryMaster;
import com.moderngas.jpaentity.DeliveryVehicleEntity;
import com.moderngas.jpaentity.OrderEntity;
import com.moderngas.jpaentity.UserEntity;
import com.moderngas.pojo.admin.DeliveryVehicleDto;
import com.moderngas.pojo.admin.FilterDto;
import com.moderngas.pojo.admin.OnboardingDto;
import com.moderngas.pojo.superadmin.AdminEntityDto;
import com.moderngas.pojo.user.AddressDto;
import com.moderngas.pojo.user.CartDto;
import com.moderngas.pojo.user.OrderDto;
import com.moderngas.pojo.user.UserDashboardDto;
import com.moderngas.pojo.user.UserEntityDto;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface GenericService {

    UserEntity convertDtoToUserData(UserEntityDto userEntityDto);

    UserEntity convertDtoToUserData(AdminEntityDto adminEntityDto) throws BadRequestException;

    UserEntityDto convertUserDataToDto(UserEntity clientEntity);

    String encodeUserPassword(String password);

    String generateRandomPassword() throws NoSuchAlgorithmException;

    AddressEntity convertDtoToAddressEntity(AddressDto addressDto);

    AddressDto convertAddressEntityToDto(AddressEntity addressEntity);

    List<UserDashboardDto> convertCategoryToDto(List<CategoryMaster> categoryMasterList);

    OrderEntity convertDtoToOrderEntity(OrderDto orderDto);

    OrderDto convertOrderEntityToDto(OrderEntity orderEntity);

    CartEntity convertDtoToCartEntity(CartDto cartDto) throws BadRequestException;

    CartDto convertCartEntityToDto(CartEntity cartEntity);

    List<OrderEntity> convertCartToOrderEntity(List<CartEntity> cartEntityList);

    OrderEntity changeOrderStatus(OrderEntity orderEntity, OrderStatus orderStatus, Long deliveryVehicleId);

    FilterDto getFilterList();

    List<DeliveryVehicleEntity> convertDtoToDeliveryVehicle(DeliveryVehicleDto deliveryVehicleDto);

    List<OnboardingDto> convertUserDateToOnboardingList(UserEntity userEntity) throws BadRequestException;

    UserEntity getUserAdminDetails() throws BadRequestException;
}
