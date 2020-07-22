package com.moderngas.service.serviceImpl;

import com.moderngas.jpaentity.AddressEntity;
import com.moderngas.jpaentity.UserEntity;
import com.moderngas.pojo.UserDashboardDto;
import com.moderngas.pojo.UserEntityDto;
import com.moderngas.repository.UserRepo;
import com.moderngas.service.EmailService;
import com.moderngas.service.GenericService;
import com.moderngas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private GenericService genericService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Override
    public String addUser(UserEntityDto userEntityDto) {
        /* Add new Client to DataBase */
        String response = "failure";
        UserEntity userEntity = genericService.convertDtoToUserData(userEntityDto);
        userEntity.setCreatedDate(new Date());
        userEntity.setUpdatedDate(new Date());
        userEntity = userRepo.save(userEntity);
        if (null != userEntity && userEntity.getId() != null) {
            response = "success";
        }
        return response;
    }

    public String updateUser(UserEntity userEntity) {
        String response = "failure";
        userEntity.setUpdatedDate(new Date());
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
        String result = "Failure";
        Optional<UserEntity> userEntity = userRepo.findByMobileNumber(mobileNumber);
        if (userEntity.isPresent()) {
            result = "Success";
        }
        return result;
    }

    @Override
    public UserEntity getUserByLoginId(Long username) {
        Optional<UserEntity> userEntity = userRepo.findByMobileNumber(username);
        return userEntity.get();
    }

    @Override
    public String changePassword(Long username, String oldPassword, String newPassword) {
        String result = "Failure";
        UserEntity userEntity = userRepo.findByMobileNumber(username).get();
        if (passwordEncoder.matches(oldPassword, userEntity.getPassword())) {
            userEntity.setPassword(passwordEncoder.encode(newPassword));
            result = updateUser(userEntity);
        }
        return result;
    }

    @Override
    public String forgetPassword(Long userName) {
        String result = "Failure";
        /* Check if User Exits */
        UserEntity userEntity = userRepo.findByMobileNumber(userName).get();
        try {
            if (null != userEntity && null != userEntity.getEmail()) {
                String tempPassword = genericService.generateRandomPassword();

                /* Send forget password mail */
                String subject = "Forget Password..?";
                emailService.sendMail(userEntity.getEmail(), subject, createEmailBody(userEntity.getName(), tempPassword));

                /* Update user with random password */
                userEntity.setPassword(passwordEncoder.encode(tempPassword));
                result = updateUser(userEntity);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String createEmailBody(String name, String tempPassword) {
        StringBuffer stringBuffer = new StringBuffer("Hi " + name + ", <Br>");
        stringBuffer.append("Have you forget your password to Modern Gas App, Don't worry we have provided a temporary password below, ");
        stringBuffer.append("<Br><Br>Password : <Strong>" + tempPassword + "</Strong>");
        stringBuffer.append("<Br>Now you may directly login to Modern Gas Account with temporary password. ");
        stringBuffer.append("<Br><Br>Thanks & Regards, <Br> A.B. Chaudhary");
        return stringBuffer.toString();
    }

    @Override
    public List<UserDashboardDto> getUserDashboard(Long userId) {
        return null;
    }

    @Override
    public String updateAddress(AddressEntity addressEntity, Long userId) {
        String response = "Failure";
        UserEntity userEntity = userRepo.findById(userId).get();
        userEntity.setAddressEntity(addressEntity);
        UserEntity savedUserEntity = userRepo.save(userEntity);
        if (null != savedUserEntity) {
            response = "success";
        }
        return response;
    }
}
