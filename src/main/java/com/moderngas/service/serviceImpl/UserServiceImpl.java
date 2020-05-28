package com.moderngas.service.serviceImpl;

import com.moderngas.jpaentity.UserEntity;
import com.moderngas.pojo.UserEntityDto;
import com.moderngas.repository.UserRepo;
import com.moderngas.service.GenericService;
import com.moderngas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private GenericService genericService;

    @Override
    public String addUser(UserEntityDto userEntityDto) {
        /* Add new Client to DataBase */
        String response = "failure";
        UserEntity userEntity = userRepo.save(genericService.convertDtoToUserData(userEntityDto));
        if (null != userEntity) {
            response = "success";
        }
        return response;
    }

    public String updateUser(UserEntity userEntity) {
        String response = "failure";
        UserEntity savedUserEntity = userRepo.save(userEntity);
        if (null != savedUserEntity) {
            response = "success";
        }
        return response;
    }

    @Override
    public List<UserEntityDto> getAllUser() {
        List<UserEntity> userEntityList = userRepo.findAll();
        List<UserEntityDto> userEntityDtoList = new ArrayList<>();
        for (UserEntity userEntity : userEntityList) {
            UserEntityDto userEntityDto = genericService.convertUserDataToDto(userEntity);
            userEntityDtoList.add(userEntityDto);
        }
        return userEntityDtoList;
    }

    @Override
    public UserEntityDto getUserById(Long userId) {
        UserEntity userEntity = userRepo.getOne(userId);
        return genericService.convertUserDataToDto(userEntity);
    }

    @Override
    public String checkUserExist(Long mobileNumber) {
        String result = "Success";
        Optional<UserEntity> userEntity = userRepo.findByMobileNumber(mobileNumber);
        if (null == userEntity) {
            result = "Failure";
        }
        return result;
    }

    @Override
    public UserEntity getUserByLoginId(Long username) {
        Optional<UserEntity> userEntity = userRepo.findByMobileNumber(username);
        return userEntity.get();
    }
}
