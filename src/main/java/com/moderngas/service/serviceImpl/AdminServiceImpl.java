package com.moderngas.service.serviceImpl;

import com.moderngas.constants.Constants;
import com.moderngas.constants.ExceptionConstants;
import com.moderngas.exception.BadRequestException;
import com.moderngas.jpaentity.AdminGasMapping;
import com.moderngas.jpaentity.UserEntity;
import com.moderngas.pojo.admin.OnboardingDto;
import com.moderngas.pojo.admin.OnboardingDtoList;
import com.moderngas.repository.UserRepo;
import com.moderngas.service.AdminService;
import com.moderngas.service.GenericService;
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

    @Override
    public List<OnboardingDto> getOnboardingDetails(Long id) throws BadRequestException {
        UserEntity userEntity = userRepo.findById(id).orElse(null);
        if (null == userEntity) {
            throw new BadRequestException(ExceptionConstants.INVALID_USER);
        }
        if (CollectionUtils.isEmpty(userEntity.getRoleEntitySet())) {
            throw new BadRequestException(ExceptionConstants.INVALID_USER_ACCESS);
        }
        List<OnboardingDto> onboardingDtoList = genericService.convertUserDateToOnboardingList(userEntity);
        return onboardingDtoList;
    }

    @Override
    public String saveOnBoardingDetails(OnboardingDtoList onboardingDtoList) throws BadRequestException {
        String response = Constants.FAILURE_STR;
        if (null == onboardingDtoList && CollectionUtils.isEmpty(onboardingDtoList.getOnboardingDtoList())) {
            return response;
        }
        UserEntity userEntity = userRepo.findById(onboardingDtoList.getId()).orElse(null);
        if (null == userEntity) {
            throw new BadRequestException(ExceptionConstants.INVALID_USER);
        }
        Set<AdminGasMapping> adminGasMappings = new HashSet<>();
        for (OnboardingDto onboardingDto : onboardingDtoList.getOnboardingDtoList()) {
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
}
