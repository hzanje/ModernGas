package com.moderngas.service;

import com.moderngas.jpaentity.UserEntity;
import com.moderngas.pojo.UserEntityDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    /**
     *
     * @param userEntityDto
     * @return
     */
    String addUser(UserEntityDto userEntityDto);

    /**
     *
     * @param userEntity
     * @return
     */
    String updateUser(UserEntity userEntity);

    /**
     *
     * @return
     */
    List<UserEntityDto> getAllUser();

    /**
     *
     * @param userId
     * @return
     */
    UserEntityDto getUserById(Long userId);

    /**
     * Check if User Exist
     *
     * @return
     */
    String checkUserExist(Long mobileNumber);


    UserEntity getUserByLoginId(Long username);

    String changePassword(Long username, String oldPassword, String newPassword);
}
