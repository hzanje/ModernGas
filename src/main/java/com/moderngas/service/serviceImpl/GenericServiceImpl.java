package com.moderngas.service.serviceImpl;

import com.moderngas.jpaentity.UserEntity;
import com.moderngas.pojo.UserEntityDto;
import com.moderngas.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class GenericServiceImpl implements GenericService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserEntity convertDtoToUserData(UserEntityDto userEntityDto) {
        UserEntity userEntity = null;
        if (null != userEntityDto) {
            userEntity = new UserEntity();
            userEntity.setName(userEntityDto.getName());
            userEntity.setEmail(userEntityDto.getEmail());
            userEntity.setAddress(userEntityDto.getAddress());
            userEntity.setMobileNumber(userEntityDto.getMobileNumber());
            userEntity.setCompanyName(userEntityDto.getCompanyName());
            userEntity.setRole(userEntityDto.getRole());
            userEntity.setContactPerson(userEntityDto.getContactPerson());
            if (null != userEntityDto.getPassword() && !userEntityDto.getPassword().isEmpty()) {
                userEntity.setPassword(encodeUserPassword(userEntity.getPassword()));
            }
        }
        return userEntity;
    }

    @Override
    public UserEntityDto convertUserDataToDto(UserEntity userEntity) {
        UserEntityDto userEntityDto = new UserEntityDto();
        userEntityDto.setId(userEntity.getId());
        userEntityDto.setName(userEntity.getName());
        userEntityDto.setEmail(userEntity.getEmail());
        userEntityDto.setAddress(userEntity.getAddress());
        userEntityDto.setMobileNumber(userEntity.getMobileNumber());
        userEntityDto.setCompanyName(userEntity.getCompanyName());
        userEntityDto.setRole(userEntity.getRole());
        userEntityDto.setContactPerson(userEntity.getContactPerson());
        return userEntityDto;
    }

    @Override
    public String encodeUserPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
