package com.moderngas.service.serviceImpl;

import com.moderngas.constants.Constants;
import com.moderngas.constants.ExceptionConstants;
import com.moderngas.enums.CylinderType;
import com.moderngas.enums.OrderStatus;
import com.moderngas.enums.UserRole;
import com.moderngas.exception.BadRequestException;
import com.moderngas.jpaentity.*;
import com.moderngas.pojo.CylinderTypeDto;
import com.moderngas.pojo.DateStatusDto;
import com.moderngas.pojo.admin.DeliveryVehicleDto;
import com.moderngas.pojo.admin.FilterDto;
import com.moderngas.pojo.admin.OnboardingDto;
import com.moderngas.pojo.superadmin.AdminEntityDto;
import com.moderngas.pojo.superadmin.GasNameCylinderTypeDto;
import com.moderngas.pojo.user.*;
import com.moderngas.repository.DeliveryVehicleRepo;
import com.moderngas.repository.GasRepo;
import com.moderngas.repository.UserRepo;
import com.moderngas.service.GenericService;
import com.moderngas.service.ResourceCentreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GenericServiceImpl implements GenericService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ResourceCentreService resourceCentreService;

    @Autowired
    private GasRepo gasRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private DeliveryVehicleRepo deliveryVehicleRepo;

    @Override
    public UserEntity convertDtoToUserData(UserEntityDto userEntityDto) throws BadRequestException {
        if (null == userEntityDto) {
            throw new BadRequestException(ExceptionConstants.INVALID_REQUEST_DATA);
        }
        UserEntity adminEntity = getUserAdminDetails();
        UserEntity userEntity = userRepo.findByMobileNumber(userEntityDto.getMobileNumber())
                .orElse(new UserEntity());
        if (null == userEntity.getId()) {
            userEntity.setName(userEntityDto.getName());
            userEntity.setEmail(userEntityDto.getEmail());
            userEntity.setMobileNumber(userEntityDto.getMobileNumber());
            userEntity.setCompanyName(userEntityDto.getCompanyName());
            userEntity.setRoleEntitySet(addUserRole(userEntityDto.getRoles(), userEntity.getRoleEntitySet()));
            userEntity.setContactPersonSet(userEntityDto.getContactPersonSet());
            if (null != userEntityDto.getPassword() && !userEntityDto.getPassword().isEmpty()) {
                userEntity.setPassword(encodeUserPassword(userEntityDto.getPassword()));
            }
            userEntity.setAdminIdSet(new HashSet<>(Arrays.asList(adminEntity.getId())));
        } else {
            Set<Long> updatedAdminIdList = userEntity.getAdminIdSet();
            updatedAdminIdList.add(adminEntity.getId());
            userEntity.setAdminIdSet(updatedAdminIdList);
        }
        return userEntity;
    }

    @Override
    public UserEntity convertDtoToUserData(AdminEntityDto adminEntityDto) throws BadRequestException {
        if (StringUtils.isEmpty(adminEntityDto.getMobileNumber())) {
            throw new BadRequestException(ExceptionConstants.USER_MOBILE_IS_EMPTY);
        }
        UserEntity userEntity = new UserEntity();
        Optional<UserEntity> user = userRepo.findByMobileNumber(adminEntityDto.getMobileNumber());
        if (user.isPresent()) {
            userEntity = user.get();
        }
        if (StringUtils.isEmpty(adminEntityDto.getEmail())) {
            throw new BadRequestException(ExceptionConstants.USER_EMAIL_IS_EMPTY);
        }
        if (StringUtils.isEmpty(adminEntityDto.getRoles())) {
            throw new BadRequestException(ExceptionConstants.USER_ROLE_IS_EMPTY);
        }
        if (CollectionUtils.isEmpty(adminEntityDto.getGasNameCylinderTypes())) {
            throw new BadRequestException(ExceptionConstants.ADMIN_GAS_IS_EMPTY);
        }
        userEntity.setName(adminEntityDto.getName());
        userEntity.setEmail(adminEntityDto.getEmail());
        userEntity.setMobileNumber(adminEntityDto.getMobileNumber());
        userEntity.setCompanyName(adminEntityDto.getCompanyName());
        userEntity.setRoleEntitySet(addUserRole(adminEntityDto.getRoles(), userEntity.getRoleEntitySet()));
        userEntity.setContactPersonSet(adminEntityDto.getContactPersonSet());
        userEntity.setAdminGasMappings(gasMappingByNameAndType(adminEntityDto.getGasNameCylinderTypes()));
        return userEntity;
    }

    private Set<AdminGasMapping> gasMappingByNameAndType(List<GasNameCylinderTypeDto> gasNameCylinderTypes) throws BadRequestException {
        if (CollectionUtils.isEmpty(gasNameCylinderTypes)) {
            return Collections.emptySet();
        }
        Set<AdminGasMapping> adminGasMappingSet = new HashSet<>();
        List<GasMaster> gasMasterList = gasRepo.getGasMasterByIdList(
                gasNameCylinderTypes.stream().map(e -> e.getId()).collect(Collectors.toList()));

        for (GasNameCylinderTypeDto nameType : gasNameCylinderTypes) {
            GasMaster gasMaster = gasMasterList.stream().filter(
                    g -> g.getId().equals(nameType.getId())).findFirst()
                    .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_GAS));
            AdminGasMapping adminGasMapping  = new AdminGasMapping();
            adminGasMapping.setGasId(gasMaster.getId());
            adminGasMapping.setGasName(gasMaster.getName());
            adminGasMapping.setCategoryId(gasMaster.getCategoryMaster().getId());
            adminGasMapping.setCategoryName(gasMaster.getCategoryMaster().getName());
            adminGasMapping.setDescription(gasMaster.getDescription());
            if (!CollectionUtils.isEmpty(nameType.getTypes())) {
                adminGasMapping.setAdminGasCylinderTypeMapping(getCylinderTypeSet(nameType.getTypes()));
            }
            adminGasMappingSet.add(adminGasMapping);
        }
        return adminGasMappingSet;
    }

    private Set<AdminGasCylinderTypeMapping> getCylinderTypeSet(List<String> types) {
        Set<AdminGasCylinderTypeMapping> selectedCylinderType = new HashSet<>();
        for (String type : types) {
            if (CylinderType.isExist(type)) {
                AdminGasCylinderTypeMapping cylinderTypeMapping = new AdminGasCylinderTypeMapping();
                cylinderTypeMapping.setCylinderType(CylinderType.getByStatus(type));
                selectedCylinderType.add(cylinderTypeMapping);
            }
        }
        return selectedCylinderType;
    }

    private Set<UserRoleEntity> addUserRole(List<String> roles, Set<UserRoleEntity> userRoleEntitySet) {
        if (CollectionUtils.isEmpty(roles)) {
            return Collections.emptySet();
        }
        if (CollectionUtils.isEmpty(userRoleEntitySet)) {
            userRoleEntitySet = new HashSet<>();
        }
        for (String role : roles) {
            UserRoleEntity userRole = new UserRoleEntity();
            userRole.setRole(UserRole.getByRole(role).getRole());
            userRoleEntitySet.add(userRole);
        }
        return userRoleEntitySet;
    }

    @Override
    public UserEntityDto convertUserDataToDto(UserEntity userEntity) throws BadRequestException {
        if (null != userEntity) {
            UserEntityDto userEntityDto = new UserEntityDto();
            userEntityDto.setId(userEntity.getId());
            userEntityDto.setName(userEntity.getName());
            userEntityDto.setEmail(userEntity.getEmail());
            userEntityDto.setMobileNumber(userEntity.getMobileNumber());
            userEntityDto.setCompanyName(userEntity.getCompanyName());
            userEntityDto.setOnboard(userEntity.isOnboarding());
            userEntityDto.setForgetPassword(userEntity.isForgetPassword());
            userEntityDto.setRoles(userEntity.getRoleEntitySet()
                    .stream().map(UserRoleEntity::getRole).collect(Collectors.toList()));
            userEntityDto.setContactPersonSet(userEntity.getContactPersonSet());
            userEntityDto = setResourceCentreForOperatorUser(userEntityDto);
            userEntityDto = setAdminDtoForSingleAdminUser(userEntityDto, userEntity);
            return userEntityDto;
        }
        return null;
    }

    private UserEntityDto setAdminDtoForSingleAdminUser(UserEntityDto userEntityDto, UserEntity userEntity) throws BadRequestException {
        if (!CollectionUtils.isEmpty(userEntityDto.getRoles()) && userEntityDto.getRoles().size() == 1) {
            UserEntity adminEntity = userRepo.findById(userEntity.getAdminIdSet().iterator().next())
                    .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_USER_ADMIN));
            userEntityDto.setAdminDto(new AdminDto(adminEntity.getId(), adminEntity.getName(),
                    adminEntity.getCompanyName(), convertAddressEntitySetToDto(adminEntity.getAddressEntitySet())));
        }
        return userEntityDto;
    }

    private UserEntityDto setResourceCentreForOperatorUser(UserEntityDto userEntityDto) throws BadRequestException {
        /*if (userEntityDto.getRoles().contains(UserRole.USER_ROLE_OPERATOR.getRole())) {
            userEntityDto.setResourceCentreDtoList(resourceCentreService.getResourceCentre());
        }*/
        return userEntityDto;
    }

    @Override
    public String encodeUserPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public String generateRandomPassword() throws NoSuchAlgorithmException {
        log.info("GenericService >> Generating Random Password");
        String capitalLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String smallLetters = "abcdefghijklmnopqrstuvwxyz";
        String specialCharacters = "!@#$*%";
        String numbers = "1234567890";
        String combinedChars = capitalLetters + smallLetters + specialCharacters + numbers;
        Random random = SecureRandom.getInstanceStrong();
        char[] password = new char[8];

        /* Create password with 1 Capital letter, 1 small Letter
        * 1 interger and 1 special character  */
        password[0] = smallLetters.charAt(random.nextInt(smallLetters.length()));
        password[1] = numbers.charAt(random.nextInt(numbers.length()));
        password[6] = capitalLetters.charAt(random.nextInt(capitalLetters.length()));
        password[7] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));

        /* Remaining character of password is generated  */
        for(int i = 2; i< 6 ; i++) {
            password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        log.info("GenericService >> Random Password is : {}", new String(password));
        return new String(password);
    }

    @Override
    public AddressEntity convertDtoToAddressEntity(AddressDto addressDto) {
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setId(addressDto.getId());
        addressEntity.setName(addressDto.getName());
        addressEntity.setMobileNumber(addressDto.getMobileNumber());
        addressEntity.setAddress1(addressDto.getAddress1());
        addressEntity.setAddress2(addressDto.getAddress2());
        addressEntity.setLandmark(addressDto.getLandmark());
        addressEntity.setCity(addressDto.getCity());
        addressEntity.setState(addressDto.getState());
        addressEntity.setPincode(addressDto.getPincode());
        addressEntity.setPrimary(addressDto.isPrimary());
        return addressEntity;
    }

    @Override
    public Set<AddressDto> convertAddressEntitySetToDto(Set<AddressEntity> addressEntitySet) {
        Set<AddressDto> addressDtoSet = new HashSet<>();
        for (AddressEntity addressEntity : addressEntitySet) {
            addressDtoSet.add(convertAddressEntityToDto(addressEntity));
        }
        return addressDtoSet;
    }

    @Override
    public AddressDto convertAddressEntityToDto(AddressEntity addressEntity) {
        AddressDto addressDto = new AddressDto();
        addressDto.setId(addressEntity.getId());
        addressDto.setName(addressEntity.getName());
        addressDto.setMobileNumber(addressEntity.getMobileNumber());
        addressDto.setAddress1(addressEntity.getAddress1());
        addressDto.setAddress2(addressEntity.getAddress2());
        addressDto.setLandmark(addressEntity.getLandmark());
        addressDto.setCity(addressEntity.getCity());
        addressDto.setState(addressEntity.getState());
        addressDto.setPincode(addressEntity.getPincode());
        addressDto.setPrimary(addressEntity.isPrimary());
        return addressDto;
    }

    @Override
    public LinkedHashSet<UserDashboardDto> convertCategoryToDto(List<CategoryMaster> categoryMasterList, LinkedHashSet<UserDashboardDto> userDashboardDtoSet) {
        if (!CollectionUtils.isEmpty(categoryMasterList)) {
            for (CategoryMaster categoryMaster : categoryMasterList) {
                UserDashboardDto userDashboardDto = new UserDashboardDto();
                userDashboardDto.setId(categoryMaster.getId());
                userDashboardDto.setName(categoryMaster.getName());
                userDashboardDtoSet.add(userDashboardDto);
            }
        }
        return userDashboardDtoSet;
    }

    @Override
    public OrderEntity convertDtoToOrderEntity(OrderDto orderDto) {
        OrderEntity orderEntity = null;
        if (null != orderDto) {
            orderEntity = new OrderEntity();
            orderEntity.setCylinderType(CylinderType.getByStatus(orderDto.getCylinderType()));
            orderEntity.setUserId(orderDto.getUserId());
            orderEntity.setAdminId(orderDto.getAdminId());
            orderEntity.setOrderStatus(OrderStatus.getByStatus(orderDto.getStatus()));
            orderEntity.setGasMaster(gasRepo.getOne(orderDto.getGasId()));
            orderEntity.setRefill(orderDto.isRefill());
            orderEntity.setRefillCount(orderDto.getRefillCount());
            orderEntity.setAddressEntity(convertDtoToAddressEntity(orderDto.getAddressDto()));
        }
        return orderEntity;
    }

    @Override
    public OrderDto convertOrderEntityToDto(OrderEntity orderEntity) {
        OrderDto orderDto = null;
        if (null != orderEntity) {
            orderDto = new OrderDto();
            orderDto.setId(orderEntity.getId());
            orderDto.setGasName(orderEntity.getGasMaster().getName());
            orderDto.setCategory(orderEntity.getGasMaster().getCategoryMaster().getName());
            orderDto.setQuantity(orderEntity.getQuantity());
            orderDto.setPrice(orderEntity.getPrice());
            orderDto.setCylinderType(orderEntity.getCylinderType().getName());
            orderDto.setRefill(orderEntity.isRefill());
            orderDto.setStatus(orderEntity.getOrderStatus().getName());
            orderDto.setUserId(orderEntity.getUserId());
            orderDto.setAdminId(orderEntity.getAdminId());
            orderDto.setGasId(orderEntity.getGasMaster().getId());
            orderDto.setDateStatusDto(convertDateStatus(orderEntity));
        }
        return orderDto;
    }

    private List<DateStatusDto> convertDateStatus(OrderEntity orderEntity) {
        List<DateStatusDto> dateStatusDtoList = new ArrayList<>();
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.getName().equals(Constants.STATUS_ORDERED)) {
                dateStatusDtoList.add(new DateStatusDto(
                        (long) orderStatus.ordinal(), orderStatus.getName(), orderEntity.getCreatedDate()));
            } else if (orderStatus.getName().equals(Constants.STATUS_LOADED)) {
                dateStatusDtoList.add(new DateStatusDto(
                        (long) orderStatus.ordinal(), orderStatus.getName(), orderEntity.getLoadedDate()));
            } else if (orderStatus.getName().equals(Constants.STATUS_DELIVERED)) {
                dateStatusDtoList.add(new DateStatusDto(
                        (long) orderStatus.ordinal(), orderStatus.getName(), orderEntity.getDeliveredDate()));
            }
        }
        return dateStatusDtoList;
    }

    @Override
    public CartEntity convertDtoToCartEntity(CartDto cartDto) throws BadRequestException {
        CartEntity cartEntity = null;
        GasMaster gasMaster = gasRepo.findById(cartDto.getGasId())
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_GAS));
        if (null != cartDto) {
            cartEntity = new CartEntity();
            cartEntity.setId(cartDto.getId());
            cartEntity.setCylinderType(CylinderType.getByStatus(cartDto.getCylinderType()));
            cartEntity.setQuantity(cartDto.getQuantity());
            cartEntity.setUserId(cartDto.getUserId());
            cartEntity.setAdminId(cartDto.getAdminId());
            cartEntity.setGasMaster(gasMaster);
        }
        return cartEntity;
    }

    @Override
    public CartDto convertCartEntityToDto(CartEntity cartEntity) {
        CartDto cartDto = null;
        if (null != cartEntity) {
            cartDto = new CartDto();
            cartDto.setId(cartEntity.getId());
            cartDto.setCylinderType(cartEntity.getCylinderType().getName());
            cartDto.setQuantity(cartEntity.getQuantity());
            cartDto.setUserId(cartEntity.getUserId());
            cartDto.setAdminId(cartEntity.getAdminId());
            if (null != cartEntity.getGasMaster()) {
                cartDto.setGasId(cartEntity.getGasMaster().getId());
                cartDto.setGasName(cartEntity.getGasMaster().getName());
                cartDto.setCategoryName(cartEntity.getGasMaster().getCategoryMaster().getName());
            }
        }
        return cartDto;
    }

    @Override
    public List<OrderEntity> convertCartToOrderEntity(List<CartEntity> cartEntityList) {
        List<OrderEntity> orderEntityList = new ArrayList<>();
        for (CartEntity cartEntity : cartEntityList) {
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setCylinderType(cartEntity.getCylinderType());
            orderEntity.setUserId(cartEntity.getUserId());
            orderEntity.setOrderStatus(OrderStatus.getByStatus("Ordered"));
            orderEntity.setGasMaster(cartEntity.getGasMaster());
            orderEntity.setRefill(cartEntity.isRefill());
            orderEntity.setRefillCount(cartEntity.getRefillCount());
            orderEntity.setQuantity(cartEntity.getQuantity());
            orderEntityList.add(orderEntity);
        }
        return orderEntityList;
    }

    @Override
    public OrderEntity changeOrderStatus(OrderEntity orderEntity, OrderStatus orderStatus, Long deliveryVehicleId) {
        log.info("GenericService >> Changes Status: {} for User: {}", orderStatus.getName(), orderEntity.getUserId());
        switch (orderStatus) {

            case ORDER_STATUS_LOADED: orderEntity.setLoadedDate(new Date());
                orderEntity.setDeliveryVehicle(deliveryVehicleRepo.getVehicleById(deliveryVehicleId));
                break;

            case ORDER_STATUS_DEVLIVERED: orderEntity.setDeliveredDate(new Date());
                break;

            case ORDER_STATUS_CANCELLED: orderEntity.setActiveFlag(false);
                orderEntity.setCancellationDate(new Date());
                break;

            default: break;
        }
        orderEntity.setOrderStatus(orderStatus);
        return orderEntity;
    }

    @Override
    public FilterDto getFilterList() {
        List<String> cylinderTypeList = new ArrayList<>();
        List<String> quantityOrdering = new ArrayList<>();
        for (CylinderType cylinderType : CylinderType.values()) {
            cylinderTypeList.add(cylinderType.getName());
        }
        quantityOrdering.add(Constants.FILTER_ORDERING_MAX_MIN);
        quantityOrdering.add(Constants.FILTER_ORDERING_MIN_MAX);
        return new FilterDto(cylinderTypeList, quantityOrdering);
    }

    @Override
    public List<DeliveryVehicleEntity> convertDtoToDeliveryVehicle(DeliveryVehicleDto deliveryVehicleDto) {
        List<DeliveryVehicleEntity> deliveryVehicleEntityList = new ArrayList<>();
        for (String vehicleName : deliveryVehicleDto.getNumbers()) {
            DeliveryVehicleEntity deliveryVehicleEntity = new DeliveryVehicleEntity();
            deliveryVehicleEntity.setNumber(vehicleName);
            deliveryVehicleEntity.setUserId(deliveryVehicleDto.getUserId());
            deliveryVehicleEntityList.add(deliveryVehicleEntity);
        }
        return deliveryVehicleEntityList;
    }

    @Override
    public List<OnboardingDto> convertUserDateToOnboardingList(UserEntity userEntity) throws BadRequestException {
        List<OnboardingDto> onBoardingDtoList = new ArrayList<>();
        if (CollectionUtils.isEmpty(userEntity.getAdminGasMappings())) {
            throw new BadRequestException(ExceptionConstants.GAS_LIST_IS_EMPTY);
        }
        Set<AdminGasMapping> adminGasMappings = userEntity.getAdminGasMappings();
        for (AdminGasMapping adminGas : adminGasMappings) {
            OnboardingDto onboardingDto = new OnboardingDto();
            onboardingDto.setId(adminGas.getId());
            onboardingDto.setGasId(adminGas.getGasId());
            onboardingDto.setGasName(adminGas.getGasName());
            onboardingDto.setCategoryId(adminGas.getCategoryId());
            onboardingDto.setCategoryName(adminGas.getCategoryName());
            onboardingDto.setDescription(adminGas.getDescription());
            onboardingDto.setPrice(adminGas.getPrice());
            onboardingDto.setCylinderTypeList(getCylinderType(adminGas.getAdminGasCylinderTypeMapping()));
            onBoardingDtoList.add(onboardingDto);
        }
        return onBoardingDtoList;
    }

    private List<CylinderTypeDto> getCylinderType(Set<AdminGasCylinderTypeMapping> adminGasCylinderTypeMapping) {
        return adminGasCylinderTypeMapping.stream()
                .map(e -> new CylinderTypeDto(e.getCylinderType().getName(), e.getCylinderType().getDescription()))
                .collect(Collectors.toList());
    }

    @Secured("ROLE_ADMIN")
    @Override
    public UserEntity getUserAdminDetails() throws BadRequestException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepo.findByMobileNumber(Long.parseLong((String) auth.getPrincipal())).orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_USER));
    }

    @Override
    public UserEntity getUserAndCheckUserAdmin(Long userId, Long adminId) throws BadRequestException {
        UserEntity userEntity = userRepo.findById(userId)
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_USER));
        if (!userEntity.getAdminIdSet().contains(adminId)) {
            throw new BadRequestException(ExceptionConstants.INVALID_USER_ADMIN);
        }
        return userEntity;
    }

    @Override
    public Set<UserDashboardDto> convertGasMappingToDashboardDto(List<AdminGasMapping> adminGasMappingList) {
        return adminGasMappingList.stream().map(e -> new UserDashboardDto(e.getCategoryId(), e.getCategoryName(), null)).collect(Collectors.toSet());
    }
}
