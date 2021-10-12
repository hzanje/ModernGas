package com.moderngas.service.serviceImpl;

import com.moderngas.constants.Constants;
import com.moderngas.constants.ExceptionConstants;
import com.moderngas.exception.BadRequestException;
import com.moderngas.jpaentity.ResourceCentreEntity;
import com.moderngas.jpaentity.UserEntity;
import com.moderngas.pojo.admin.ResourceCentreDto;
import com.moderngas.pojo.admin.UserDetails;
import com.moderngas.repository.ResourceCentreRepo;
import com.moderngas.repository.UserRepo;
import com.moderngas.service.GenericService;
import com.moderngas.service.ResourceCentreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ResourceCentreServiceImpl implements ResourceCentreService {

    @Autowired
    private GenericService genericService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ResourceCentreRepo resourceCentreRepo;

    @Override
    public String addOrUpdateResourceCentre(List<ResourceCentreDto> resourceCentreDtoList) throws BadRequestException {
        if (CollectionUtils.isEmpty(resourceCentreDtoList)) {
            return Constants.FAILURE_STR;
        }
        UserEntity userEntity = genericService.getUserAdminDetails();
        Set<ResourceCentreEntity> resourceCentreEntityList = resourceCentreDtoList.stream()
                .map(r -> new ResourceCentreEntity(r.getId(), r.getName(), r.getAlias())).collect(Collectors.toSet());
        userEntity.setResourceCentreEntitySet(resourceCentreEntityList);
        userRepo.save(userEntity);
        return Constants.SUCCESS_STR;
    }

    @Override
    public List<ResourceCentreDto> getResourceCentre() throws BadRequestException {
        UserEntity userEntity = genericService.getUserAdminDetails();
        return resourceCentreRepo.getResourceCentreByAdminId(userEntity.getId());
    }

    @Override
    public String deleteResourceCentre(Long id) throws BadRequestException {
        resourceCentreRepo.deleteById(id);
        return Constants.SUCCESS_STR;
    }
}
