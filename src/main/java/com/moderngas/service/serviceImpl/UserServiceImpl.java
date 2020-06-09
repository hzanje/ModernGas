package com.moderngas.service.serviceImpl;

import com.moderngas.jpaentity.UserEntity;
import com.moderngas.pojo.UserEntityDto;
import com.moderngas.repository.UserRepo;
import com.moderngas.service.EmailService;
import com.moderngas.service.GenericService;
import com.moderngas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Override
    public String addUser(UserEntityDto userEntityDto) {
        /* Add new Client to DataBase */
        String response = "failure";
        UserEntity userEntity = genericService.convertDtoToUserData(userEntityDto);
        userEntity.setPassword(passwordEncoder.encode(userEntityDto.getPassword()));
        userEntity = userRepo.save(userEntity);
        if (null != userEntity && userEntity.getId() != null) {
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

    @Override
    public String changePassword(Long username, String oldPassword, String newPassword) {
        String result = "Failure";
        UserEntity userEntity = userRepo.findByMobileNumber(username).get();
        if (passwordEncoder.matches(oldPassword, userEntity.getPassword())) {
            userEntity.setPassword(passwordEncoder.encode(newPassword));
            updateUser(userEntity);
            result =  "Success";
        }
        return result;
    }

    @Override
    public String forgetPassword(Long userName) {
        String result = "Failure";
        /* Check if User Exits */
        UserEntity userEntity = userRepo.findByMobileNumber(userName).get();
        if (null != userEntity && null != userEntity.getEmail()) {
            String tempPassword = genericService.generateRandomPassword();

            /* Send forget password mail */
            String subject = "Forget Password..?";
            emailService.sendMail(userEntity.getEmail(), subject, createEmailBody(userEntity.getName(), tempPassword));

            /* Update user with random password */


            result = "Success";
        }
        return result;
    }

    private String createEmailBody(String name, String tempPassword) {
        StringBuffer stringBuffer = new StringBuffer("Hi " + name + ", /n");
        stringBuffer.append("You have requested to reset password for your Modern Gas Account. We have provided the temporary passeord to login your account /n/n");
        stringBuffer.append("Password : " + tempPassword);
        stringBuffer.append("If you didn't request for forget password, You can ignore the email you'r password will not be changed. /n/n");
        stringBuffer.append("Thanks & Regards, /n A.B. Chaudhary");
        return stringBuffer.toString();
    }
}
