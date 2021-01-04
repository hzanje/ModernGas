package com.moderngas.service;

import com.moderngas.jpaentity.AddressEntity;
import com.moderngas.jpaentity.UserEntity;
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

    UserEntityDto getUserById(Long userId);

    String checkUserExist(Long mobileNumber);

    UserEntity getUserByLoginId(Long username);

    String changePassword(Long username, String oldPassword, String newPassword);

    String forgetPassword(Long userName);

    List<UserDashboardDto> getUserDashboard(Long userId);

    String updateAddress(AddressEntity addressEntity, Long userId);
    
    JSONObject getAddress(Long userId);

    String refreshToken(String existingToken);

    List<NameIdDto> getListByCategoryId(Long categoryId);

    GasDto getGasDetailsById(Long id, Long userId);
}
