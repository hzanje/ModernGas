package com.moderngas.service.serviceImpl;

import com.moderngas.constants.Constants;
import com.moderngas.enums.UserRole;
import com.moderngas.exception.BadRequestException;
import com.moderngas.jpaentity.UserEntity;
import com.moderngas.pojo.superadmin.AdminEntityDto;
import com.moderngas.repository.UserRepo;
import com.moderngas.service.GenericService;
import com.moderngas.service.SuperAdminService;
import com.moderngas.service.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class SuperAdminServiceImpl implements SuperAdminService {

    private static Logger log = LoggerFactory.getLogger(SuperAdminServiceImpl.class.getName());

    @Autowired
    private GenericService genericService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ValidationService validationService;

    @Override
    public String createAdmin(Long superId, AdminEntityDto adminEntityDto) throws BadRequestException {
        /* Add new Client to DataBase */
        UserEntity superEntity = validationService.validateSuperAdminEntity(superId);
        UserEntity adminEntity = new UserEntity();
        if (!ObjectUtils.isEmpty(adminEntityDto.getUserDto()) && !ObjectUtils.isEmpty(adminEntityDto.getUserDto().getId())) {
            adminEntity = validationService.validateAdminEntity(adminEntityDto.getUserDto().getId());
        }
        adminEntity = genericService.convertUserDtoToEntity(adminEntity, adminEntityDto.getUserDto(), superEntity, UserRole.USER_ROLE_ADMIN);
        adminEntity.setContactPersonSet(adminEntityDto.getContactPersonSet());
        adminEntity.setAdminGasMappings(genericService.gasMappingByNameAndType(adminEntityDto.getGasNameCylinderTypes()));
        userRepo.save(adminEntity);
        return Constants.SUCCESS_STR;
    }

}
