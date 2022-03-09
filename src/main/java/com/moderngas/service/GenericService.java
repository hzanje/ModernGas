package com.moderngas.service;

import com.moderngas.enums.OrderStatus;
import com.moderngas.enums.UserPrivilege;
import com.moderngas.enums.UserRole;
import com.moderngas.exception.BadRequestException;
import com.moderngas.jpaentity.*;
import com.moderngas.pojo.UserDto;
import com.moderngas.pojo.admin.CylinderDto;
import com.moderngas.pojo.admin.DeliveryVehicleDto;
import com.moderngas.pojo.admin.FilterDto;
import com.moderngas.pojo.admin.OnboardingDto;
import com.moderngas.pojo.employee.PrivilegeDto;
import com.moderngas.pojo.superadmin.GasNameCylinderTypeDto;
import com.moderngas.pojo.user.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;

public interface GenericService {


    UserEntity convertUserDtoToEntity(UserEntity entity, UserDto userDto, UserEntity adminEntity, UserRole userRole) throws BadRequestException;

    Set<Long> addOrUpdateUserAdmin(UserEntity entity, Long adminId) throws BadRequestException;

    Set<UserRoleEntity> addOrUpdateUserRoleAndPrivilege(UserEntity entity, Set<String> privilegeSet, UserRole userRoleUser) throws BadRequestException;

    Set<UserPrivilegeEntity> addOrUpdateUserPrivilege(Set<UserPrivilegeEntity> existingPrivilege, List<UserPrivilege> privilegeList) throws BadRequestException;

    UserEntityResponseDto convertUserDataToDto(UserEntity clientEntity) throws BadRequestException;

    Set<AdminGasMapping> gasMappingByNameAndType(List<GasNameCylinderTypeDto> gasNameCylinderTypes) throws BadRequestException;

    String encodeUserPassword(String password);

    String generateRandomPassword() throws NoSuchAlgorithmException;

    Integer generateRandomOrderNumber() throws BadRequestException;

    AddressEntity convertDtoToAddressEntity(AddressDto addressDto, AddressEntity addressEntity);

    Set<AddressDto> convertAddressEntitySetToDto(Set<AddressEntity> addressEntity);

    AddressDto convertAddressEntityToDto(AddressEntity addressEntity);

    Set<UserDashboardDto> convertCategoryToDto(List<CategoryMaster> categoryMasterList, Set<UserDashboardDto> hashSet);

    OrderEntity convertDtoToOrderEntity(OrderDto orderDto) throws BadRequestException;

    OrderDto convertOrderEntityToDto(OrderEntity orderEntity);

    CartEntity convertDtoToCartEntity(CartDto cartDto) throws BadRequestException;

    CartDto convertCartEntityToDto(CartEntity cartEntity);

    CylinderEntity convertDtoToCylinderEntity(UserEntity entity, CylinderDto cylinderDto, String role) throws BadRequestException;

    List<OrderEntity> convertCartToOrderEntity(List<CartEntity> cartEntityList, Long addressId) throws BadRequestException;

    OrderEntity changeOrderStatus(OrderEntity orderEntity, OrderStatus orderStatus, Long deliveryVehicleId);

    FilterDto getFilterList();

    List<DeliveryVehicleEntity> convertDtoToDeliveryVehicle(DeliveryVehicleDto deliveryVehicleDto);

    List<OnboardingDto> convertUserDateToOnboardingList(UserEntity userEntity) throws BadRequestException;

    UserEntity getUserAdminDetails() throws BadRequestException;

    UserEntity getUserAndCheckUserAdmin(Long userId, Long adminId) throws BadRequestException;

    Set<UserDashboardDto> convertGasMappingToDashboardDto(List<AdminGasMapping> adminGasMappingList);

    Set<PrivilegeDto> convertToPrivilegeDto(Set<UserPrivilegeEntity> userPrivilegeEntitySet) throws BadRequestException;

    UserEntity updateUserAdminForUser(UserEntity entity, Long adminId);
}
