package com.moderngas.service.serviceImpl;

import com.moderngas.constants.Constants;
import com.moderngas.constants.ExceptionConstants;
import com.moderngas.enums.CylinderType;
import com.moderngas.exception.BadRequestException;
import com.moderngas.jpaentity.*;
import com.moderngas.pojo.CylinderTypeIdPriceDto;
import com.moderngas.pojo.admin.OnboardingDto;
import com.moderngas.pojo.admin.OnboardingDtoList;
import com.moderngas.pojo.admin.ProductGasDto;
import com.moderngas.repository.AdminGasMappingRepo;
import com.moderngas.repository.UserGasMappingRepo;
import com.moderngas.repository.UserRepo;
import com.moderngas.service.AdminService;
import com.moderngas.service.GenericService;
import com.moderngas.service.ValidationService;
import lombok.NonNull;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AdminServiceImpl implements AdminService {

    private static Logger log = LoggerFactory.getLogger(AdminServiceImpl.class.getName());

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private GenericService genericService;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private AdminGasMappingRepo adminGasMappingRepo;

    @Autowired
    private UserGasMappingRepo userGasMappingRepo;

    @Override
    public List<OnboardingDto> getOnboardingDetails(Long id) throws BadRequestException {
        log.info("AdminService >>> Get Admin OnBoarding for {}", id
        );
        UserEntity userEntity = validationService.validateUserEntity(id);
        if (CollectionUtils.isEmpty(userEntity.getRoleEntitySet())) {
            throw new BadRequestException(ExceptionConstants.INVALID_USER_ACCESS);
        }
        return genericService.convertUserDateToOnboardingList(userEntity);
    }

    @Override
    public String saveOnBoardingDetails(OnboardingDtoList onboardingDtoList) throws BadRequestException {
        log.info("AdminService :: saveOnBoardingDetails >>>");
        String response = Constants.FAILURE_STR;
        if (CollectionUtils.isEmpty(onboardingDtoList.getOnBoardingListDto())) {
            return response;
        }
        UserEntity userEntity = validationService.validateUserEntity(onboardingDtoList.getId());
        Set<AdminGasMapping> adminGasMappings = new HashSet<>();
        for (OnboardingDto onboardingDto : onboardingDtoList.getOnBoardingListDto()) {
            AdminGasMapping adminGas = userEntity.getAdminGasMappings()
                    .stream().filter(a -> a.getId().equals(onboardingDto.getId()))
                    .findAny().orElse(null);
            if (null == adminGas) {
                continue;
            }
            adminGas.setDescription(onboardingDto.getDescription());
            adminGasMappings.add(adminGas);
        }
        userEntity.setAdminGasMappings(adminGasMappings);
        userRepo.save(userEntity);
        response = Constants.SUCCESS_STR;
        return response;
    }

    @Override
    public String updateAdminGas(Long adminId, Long userId, @NonNull ProductGasDto productGasDto) throws BadRequestException {
        log.info("AdminService :: updateAdminGas >>> adminId : {}, userId : {}", adminId, userId);
        UserEntity adminEntity = validationService.validateAdminEntity(adminId);
        GasMaster gasMaster = validationService.validateGasMaster(productGasDto.getId());

        AdminGasMapping adminGasMapping = adminGasMappingRepo.getGasMappingByGasIdAndAdminId(gasMaster.getId(), adminEntity.getId());
        if (null == adminGasMapping) {
            throw new BadRequestException(ExceptionConstants.ADMIN_GAS_IS_EMPTY);
        }
        if (!ObjectUtils.isEmpty(userId)) {
            UserEntity userEntity = validationService.validateUserEntity(userId);
            UserGasMapping userGasMapping = saveAndUpdateUserGasMapping(userEntity, adminEntity, adminGasMapping, productGasDto);
            userGasMappingRepo.save(userGasMapping);
        } else {
            adminGasMapping.setDescription(productGasDto.getDescription());
            adminGasMapping.setAdminGasCylinderTypeMapping(createAdminGasCylinderTypeList(productGasDto.getTypeIdPriceDtoList(), adminGasMapping));
            adminGasMappingRepo.save(adminGasMapping);
        }
        return Constants.SUCCESS_STR;
    }

    private UserGasMapping saveAndUpdateUserGasMapping(UserEntity userEntity, UserEntity adminEntity,
                                                       AdminGasMapping adminGasMapping, ProductGasDto productGasDto) {
        UserGasMapping userGasMapping = userGasMappingRepo.getGasMappingByGasIdAndAdminIdAndUserId(adminGasMapping.getGasId(), adminEntity.getId(), userEntity.getId());
        if (ObjectUtils.isEmpty(userGasMapping)) {
            userGasMapping = new UserGasMapping();
        }
        userGasMapping.setGasId(adminGasMapping.getGasId());
        userGasMapping.setUserId(userEntity.getId());
        userGasMapping.setAdminId(adminEntity.getId());
        userGasMapping.setUserGasCylinderTypeMapping(createUserGasCylinderTypeList(productGasDto.getTypeIdPriceDtoList(), userGasMapping));
        return userGasMapping;
    }

    private Set<AdminGasCylinderTypeMapping> createAdminGasCylinderTypeList(List<CylinderTypeIdPriceDto> typeIdPriceDtoList, AdminGasMapping adminGasMapping) {
        if (CollectionUtils.isEmpty(typeIdPriceDtoList)) {
            return null;
        }
        Set<AdminGasCylinderTypeMapping> adminGasCylinderTypeMappingSet = adminGasMapping.getAdminGasCylinderTypeMapping();
        for (CylinderTypeIdPriceDto typeIdPriceDto : typeIdPriceDtoList) {
            AdminGasCylinderTypeMapping cylinderTypeMapping = adminGasMapping.getAdminGasCylinderTypeMapping().stream()
                    .filter(e -> e.getCylinderType().getName().equals(typeIdPriceDto.getType())).findFirst().orElse(null);
            if (ObjectUtils.isEmpty(cylinderTypeMapping)) {
                continue;
            }
            cylinderTypeMapping.setPrice(typeIdPriceDto.getPrice());
        }
        return adminGasCylinderTypeMappingSet;
    }

    private Set<UserGasCylinderTypeMapping> createUserGasCylinderTypeList(List<CylinderTypeIdPriceDto> typeIdPriceDtoList, UserGasMapping userGasMapping) {
        if (CollectionUtils.isEmpty(typeIdPriceDtoList)) {
            return null;
        }
        Set<UserGasCylinderTypeMapping> userGasCylinderTypeMappingSet = new HashSet<>();
        for (CylinderTypeIdPriceDto typeIdPriceDto : typeIdPriceDtoList) {
            UserGasCylinderTypeMapping userGasCylinderTypeMapping = new UserGasCylinderTypeMapping();
            userGasCylinderTypeMapping.setCylinderType(CylinderType.getByStatus(typeIdPriceDto.getType()));
            userGasCylinderTypeMapping.setPrice(typeIdPriceDto.getPrice());
            userGasCylinderTypeMappingSet.add(userGasCylinderTypeMapping);
        }
        return userGasCylinderTypeMappingSet;
    }
}
