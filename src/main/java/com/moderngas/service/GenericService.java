package com.moderngas.service;

import com.moderngas.jpaentity.AddressEntity;
import com.moderngas.jpaentity.CategoryMaster;
import com.moderngas.jpaentity.UserEntity;
import com.moderngas.pojo.AddressDto;
import com.moderngas.pojo.UserDashboardDto;
import com.moderngas.pojo.UserEntityDto;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;


public interface GenericService {

    /**
     * Converting User DTO to its Respective Entity.
     *
     * @param userEntityDto
     * @return
     */
    UserEntity convertDtoToUserData(UserEntityDto userEntityDto);

    /**
     * Converting User Entity to its Respective DTO.
     *
     * @param clientEntity
     * @return
     */
    UserEntityDto convertUserDataToDto(UserEntity clientEntity);

    /**
     * Encoding the User's Password with SHA256 Hashing
     *
     * @param password
     * @return
     */
    String encodeUserPassword(String password);

    String generateRandomPassword();

    AddressEntity convertDtoToAddressEntity(AddressDto addressDto);

    AddressDto convertAddressEntityToDto(AddressEntity addressEntity);

    List<UserDashboardDto> convertCategoryToDto(List<CategoryMaster> categoryMasterList, Long userId);
}
