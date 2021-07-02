package com.moderngas.service.serviceImpl;

import com.moderngas.constants.Constants;
import com.moderngas.exception.BadRequestException;
import com.moderngas.jpaentity.UserEntity;
import com.moderngas.pojo.superadmin.AdminEntityDto;
import com.moderngas.repository.UserRepo;
import com.moderngas.service.GenericService;
import com.moderngas.service.SuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SuperAdminServiceImpl implements SuperAdminService {

    @Autowired
    private GenericService genericService;

    @Autowired
    private UserRepo userRepo;

    @Override
    public String createAdmin(AdminEntityDto adminEntityDto) throws BadRequestException {
        /* Add new Client to DataBase */
        String response = Constants.FAILURE_STR;
        UserEntity userEntity = genericService.convertDtoToUserData(adminEntityDto);
        userRepo.save(userEntity);

        /* Generate password and send the password to user via email and sms */
        if (userEntity.getId() != null) {
            response = Constants.SUCCESS_STR;
        }
        return response;
    }

}
