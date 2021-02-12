package com.moderngas.service;

import com.moderngas.exception.BadRequestException;
import com.moderngas.jpaentity.AddressEntity;
import com.moderngas.jpaentity.UserEntity;
import com.moderngas.pojo.admin.DeliveryVehicleDto;
import com.moderngas.pojo.user.GasDto;
import com.moderngas.pojo.NameIdDto;
import com.moderngas.pojo.user.UserDashboardDto;
import com.moderngas.pojo.user.UserEntityDto;

import net.minidev.json.JSONObject;

import java.util.List;

public interface UserService {

    String addUser(UserEntityDto userEntityDto);

    String updateUser(UserEntity userEntity);

    List<UserEntityDto> getAllUser();

    UserEntityDto getUserById(Long userId) throws BadRequestException;

    String checkUserExist(Long mobileNumber);

    UserEntity getUserByLoginId(Long username);

    String changePassword(Long username, String oldPassword, String newPassword);

    String forgetPassword(Long userName) throws BadRequestException;

    List<UserDashboardDto> getUserDashboard(Long userId);

    String updateAddress(AddressEntity addressEntity, Long userId);
    
    JSONObject getAddress(Long userId);

    String refreshToken(String existingToken);

    List<NameIdDto> getListByCategoryId(Long categoryId);

    GasDto getGasDetailsById(Long id) throws BadRequestException;

    void checkIfRoleIsNotUser(UserEntity userEntity) throws BadRequestException;

    String addVehicle(DeliveryVehicleDto deliveryVehicleDto) throws BadRequestException;

    List<NameIdDto> getVehicleNumberList(Long userId);
}
