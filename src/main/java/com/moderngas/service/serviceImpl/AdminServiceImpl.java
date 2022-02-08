package com.moderngas.service.serviceImpl;

import com.moderngas.constants.Constants;
import com.moderngas.constants.ExceptionConstants;
import com.moderngas.exception.BadRequestException;
import com.moderngas.jpaentity.AdminGasMapping;
import com.moderngas.jpaentity.GasMaster;
import com.moderngas.jpaentity.UserEntity;
import com.moderngas.pojo.admin.OnboardingDto;
import com.moderngas.pojo.admin.OnboardingDtoList;
import com.moderngas.pojo.admin.ProductGasDto;
import com.moderngas.repository.AdminGasMappingRepo;
import com.moderngas.repository.UserRepo;
import com.moderngas.service.AdminService;
import com.moderngas.service.GenericService;
import com.moderngas.service.ValidationService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private GenericService genericService;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private AdminGasMappingRepo adminGasMappingRepo;

    @Override
    public List<OnboardingDto> getOnboardingDetails(Long id) throws BadRequestException {
        UserEntity userEntity = validationService.validateUserEntity(id);
        if (CollectionUtils.isEmpty(userEntity.getRoleEntitySet())) {
            throw new BadRequestException(ExceptionConstants.INVALID_USER_ACCESS);
        }
        return genericService.convertUserDateToOnboardingList(userEntity);
    }

    @Override
    public String saveOnBoardingDetails(OnboardingDtoList onboardingDtoList) throws BadRequestException {
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
            adminGas.setPrice(onboardingDto.getPrice());
            adminGasMappings.add(adminGas);
        }
        userEntity.setAdminGasMappings(adminGasMappings);
        userRepo.save(userEntity);
        response = Constants.SUCCESS_STR;
        return response;
    }

    @Override
    public String updateAdminGas(Long adminId, @NonNull ProductGasDto productGasDto) throws BadRequestException {
        UserEntity adminEntity = validationService.validateAdminEntity(adminId);
        GasMaster gasMaster = validationService.validateGasMaster(productGasDto.getId());
        AdminGasMapping adminGasMapping = adminGasMappingRepo.getGasMappingByGasIdAndAdminId(gasMaster.getId(), adminEntity.getId());
        if (null == adminGasMapping) {
            throw new BadRequestException(ExceptionConstants.ADMIN_GAS_IS_EMPTY);
        }
        adminGasMapping.setDescription(productGasDto.getDescription());
        adminGasMapping.setPrice(productGasDto.getPrice());
        adminGasMappingRepo.save(adminGasMapping);
        return Constants.SUCCESS_STR;
    }
}
