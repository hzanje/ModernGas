package com.moderngas.service;

import com.moderngas.exception.BadRequestException;
import com.moderngas.jpaentity.AddressEntity;
import com.moderngas.jpaentity.UserEntity;
import com.moderngas.pojo.admin.DeliveryVehicleDto;
import com.moderngas.pojo.admin.UserDetails;
import com.moderngas.pojo.user.GasDto;
import com.moderngas.pojo.NameIdDto;
import com.moderngas.pojo.user.UserDashboardDto;
import com.moderngas.pojo.user.UserEntityDto;

import com.moderngas.pojo.user.UserSearchDto;
import net.minidev.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    String addUser(UserEntityDto userEntityDto) throws BadRequestException;

    String updateUser(UserEntity userEntity);

    List<UserEntityDto> getAllUserByAdmin(Long adminId) throws BadRequestException;

    UserEntityDto getUserById(Long userId) throws BadRequestException;

    String checkUserExist(Long mobileNumber);

    UserEntity getUserByLoginId(Long username) throws BadRequestException;

    String changePassword(Long username, String newPassword) throws BadRequestException;

    String forgetPassword(Long userName) throws BadRequestException;

    List<UserDashboardDto> getUserDashboard(Long userId, Long adminId) throws BadRequestException;

    String updateAddress(AddressEntity addressEntity, Long userId) throws BadRequestException;
    
    JSONObject getAddress(Long userId) throws BadRequestException;

    String refreshToken(String existingToken) throws BadRequestException;

    List<NameIdDto> getGasListByCategoryId(Long categoryId);

    GasDto getGasDetailsById(Long id, Long adminId) throws BadRequestException;

    void checkIfRoleIsNotUser(UserEntity userEntity) throws BadRequestException;

    String addVehicle(DeliveryVehicleDto deliveryVehicleDto) throws BadRequestException;

    List<NameIdDto> getVehicleNumberList(Long userId);

    Page<UserSearchDto> searchUserByName(Pageable pageable, String name) throws BadRequestException;

    UserDetails getUserDetailsForAdmin(Long id) throws BadRequestException;

    String logout(String token) throws BadRequestException;
}
