package com.moderngas.service.serviceImpl;

import com.moderngas.constants.Constants;
import com.moderngas.constants.ExceptionConstants;
import com.moderngas.enums.*;
import com.moderngas.exception.BadRequestException;
import com.moderngas.jpaentity.*;
import com.moderngas.pojo.CylinderTypeDto;
import com.moderngas.pojo.OrderDateStatusDto;
import com.moderngas.pojo.UserDto;
import com.moderngas.pojo.admin.CylinderDto;
import com.moderngas.pojo.admin.DeliveryVehicleDto;
import com.moderngas.pojo.admin.FilterDto;
import com.moderngas.pojo.admin.OnboardingDto;
import com.moderngas.pojo.employee.PrivilegeDto;
import com.moderngas.pojo.superadmin.GasNameCylinderTypeDto;
import com.moderngas.pojo.user.*;
import com.moderngas.repository.*;
import com.moderngas.service.GenericService;
import com.moderngas.service.ResourceCentreService;
import com.moderngas.service.ValidationService;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GenericServiceImpl implements GenericService {

    private static Logger log = LoggerFactory.getLogger(GenericServiceImpl.class.getName());

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

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private InventoryRepo inventoryRepo;

    @Override
    public UserEntity convertUserDtoToEntity(UserEntity entity, @NonNull UserDto userDto, UserEntity adminEntity, UserRole userRole) throws BadRequestException {
        entity.setName(userDto.getName());
        entity.setMobileNumber(userDto.getMobileNumber());
        entity.setEmail(userDto.getEmail());
        Set<String> privilegeSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(userDto.getPrivilegeDtoList())) {
            privilegeSet = userDto.getPrivilegeDtoList().stream()
                    .filter(PrivilegeDto::isActive)
                    .map(PrivilegeDto::getPrivilege).collect(Collectors.toSet());
        }
        entity.setRoleEntitySet(addOrUpdateUserRoleAndPrivilege(entity, privilegeSet, userRole));
        return entity;
    }

    @Override
    public Set<Long> addOrUpdateUserAdmin(UserEntity entity, Long adminId) throws BadRequestException {
        Set<Long> adminIdSet = new HashSet<>();
        if (!ObjectUtils.isEmpty(entity.getId())) {
            adminIdSet = entity.getAdminIdSet();
        }
        adminIdSet.add(adminId);
        return adminIdSet;
    }

    @Override
    public Set<UserRoleEntity> addOrUpdateUserRoleAndPrivilege(UserEntity entity, Set<String> privilegeSet, UserRole userRole) throws BadRequestException {
        Set<UserRoleEntity> roleEntitySet = new HashSet<>();
        if (!CollectionUtils.isEmpty(entity.getRoleEntitySet())) {
            roleEntitySet = entity.getRoleEntitySet();
        }
        UserRoleEntity userRoleEntity = roleEntitySet.stream().filter(e -> e.getRole().equals(userRole.getRole()))
                .findAny().orElse(new UserRoleEntity());
        userRoleEntity.setRole(userRole.getRole());
        if (UserRole.USER_ROLE_EMPLOYEE == userRole && !CollectionUtils.isEmpty(privilegeSet)) {
            List<UserPrivilege> privilegeList = new ArrayList<>();
            for (String privilegeName : privilegeSet) {
                privilegeList.add(UserPrivilege.getUserPrivilegeByName(privilegeName));
            }
            userRoleEntity.setUserPrivilegeSet(addOrUpdateUserPrivilege(userRoleEntity.getUserPrivilegeSet(), privilegeList));
        }
        roleEntitySet.add(userRoleEntity);
        return roleEntitySet;
    }

    @Secured("ROLE_EMPLOYEE")
    @Override
    public Set<UserPrivilegeEntity> addOrUpdateUserPrivilege(Set<UserPrivilegeEntity> existingPrivilege, List<UserPrivilege> privilegeList) throws BadRequestException {
        Set<UserPrivilegeEntity> userPrivilegeEntitySet = new HashSet<>();
        for (UserPrivilege userPrivilege : privilegeList) {
            UserPrivilegeEntity userPrivilegeEntity = new UserPrivilegeEntity();
            userPrivilegeEntity.setPrivilege(userPrivilege.getPrivilege());
            userPrivilegeEntitySet.add(userPrivilegeEntity);
        }
        return userPrivilegeEntitySet;
    }

    /**
     * Get Gas Mapping By Name and Type(Category)
     * Create The Mapping of Admin Gas while creation of Admin
     *
     * @param gasNameCylinderTypes
     * @return AdminGasMapping Set
     * @throws BadRequestException
     */
    @Override
    public Set<AdminGasMapping> gasMappingByNameAndType(List<GasNameCylinderTypeDto> gasNameCylinderTypes) throws BadRequestException {
        if (CollectionUtils.isEmpty(gasNameCylinderTypes)) {
            return Collections.emptySet();
        }
        Set<AdminGasMapping> adminGasMappingSet = new HashSet<>();
        List<GasMaster> gasMasterList = gasRepo.getGasMasterByIdList(
                gasNameCylinderTypes.stream().map(GasNameCylinderTypeDto::getId).toList());

        for (GasNameCylinderTypeDto nameType : gasNameCylinderTypes) {
            // Get Gas Master as per Dto
            GasMaster gasMaster = gasMasterList.stream().filter(
                    g -> g.getId().equals(nameType.getId())).findFirst()
                    .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_GAS));
            AdminGasMapping adminGasMapping = new AdminGasMapping();
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


    @Override
    public UserEntityResponseDto convertUserDataToDto(UserEntity userEntity) throws BadRequestException {
        if (null != userEntity) {
            UserEntityResponseDto userEntityResponseDto = new UserEntityResponseDto();
            userEntityResponseDto.setId(userEntity.getId());
            userEntityResponseDto.setName(userEntity.getName());
            userEntityResponseDto.setEmail(userEntity.getEmail());
            userEntityResponseDto.setMobileNumber(userEntity.getMobileNumber());
            userEntityResponseDto.setCompanyName(userEntity.getCompanyName());
            userEntityResponseDto.setOnboard(userEntity.isOnboarding());
            userEntityResponseDto.setForgetPassword(userEntity.isForgetPassword());
            userEntityResponseDto.setRoles(userEntity.getRoleEntitySet()
                    .stream().map(UserRoleEntity::getRole).toList());
            UserRoleEntity employeeRole = userEntity.getRoleEntitySet().stream()
                    .filter(e -> e.getRole().equals(UserRole.USER_ROLE_EMPLOYEE.getRole()))
                    .findFirst().orElse(null);
            userEntityResponseDto.setPrivilegeDtoList(ObjectUtils.isEmpty(employeeRole) ? null : convertToPrivilegeDto(employeeRole.getUserPrivilegeSet()));
            setResourceCentreForOperatorUser(userEntityResponseDto);
            setAdminDtoForSingleAdminUser(userEntityResponseDto, userEntity);
            return userEntityResponseDto;
        }
        return null;
    }

    private UserEntityResponseDto setAdminDtoForSingleAdminUser(UserEntityResponseDto userEntityResponseDto, UserEntity userEntity) throws BadRequestException {
        List<AdminDto> adminDtoList = new ArrayList<>();
        for (Long adminId : userEntity.getAdminIdSet()) {
            UserEntity adminEntity = validationService.validateAdminEntity(adminId);
            adminDtoList.add(new AdminDto(adminEntity.getId(), adminEntity.getName(),
                    adminEntity.getCompanyName()));
        }
        userEntityResponseDto.setAdminDtoList(adminDtoList);
        return userEntityResponseDto;
    }

    private UserEntityResponseDto setResourceCentreForOperatorUser(UserEntityResponseDto userEntityResponseDto) throws BadRequestException {
        if (userEntityResponseDto.getRoles().contains(UserRole.USER_ROLE_EMPLOYEE.getRole())) {
            userEntityResponseDto.setResourceCentreDtoList(resourceCentreService.getResourceCentre());
        }
        return userEntityResponseDto;
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
        for (int i = 2; i < 6; i++) {
            password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        log.info("GenericService >> Random Password is : {}", new String(password));
        return new String(password);
    }

    @Override
    public Integer generateRandomOrderNumber() throws BadRequestException {
        try {
            Random random = SecureRandom.getInstanceStrong();
            int min = 0;
            int max = 50000;
            return random.ints(min,(max + 1)).findFirst().getAsInt();
        } catch (NoSuchAlgorithmException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public AddressEntity convertDtoToAddressEntity(AddressDto addressDto, AddressEntity addressEntity) {
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
    public Set<UserDashboardDto> convertCategoryToDto(List<CategoryMaster> categoryMasterList, Set<UserDashboardDto> userDashboardDtoSet) {
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
    public OrderEntity convertDtoToOrderEntity(@NonNull OrderDto orderDto) throws BadRequestException {
        UserEntity userEntity = validationService.validateUserEntity(orderDto.getUserId());
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCylinderType(CylinderType.getByStatus(orderDto.getCylinderType()));
        orderEntity.setUserId(orderDto.getUserId());
        orderEntity.setAdminId(orderDto.getAdminId());
        orderEntity.setOrderStatus(OrderStatus.getByStatus(orderDto.getStatus()));
        orderEntity.setGasMaster(validationService.validateGasMaster(orderDto.getGasId()));
        orderEntity.setRefill(orderDto.isRefill());
        orderEntity.setOrderNumber("REEK_ORD_" + String.format("%05d", generateRandomOrderNumber()));
        orderEntity.setRefillCount(orderDto.getRefillCount());
        orderEntity.setAddressEntity(userEntity.getAddressEntitySet().stream()
                .filter(e -> e.getId().equals(orderDto.getAddressDto().getId())).findFirst()
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_USER_ADDRESS)));
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
            orderDto.setOrderDateStatusDto(convertDateStatus(orderEntity));
            orderDto.setDeliveryVehicle(ObjectUtils.isEmpty(orderEntity.getDeliveryVehicle()) ? "" : orderEntity.getDeliveryVehicle().getNumber());
        }
        return orderDto;
    }

    private List<OrderDateStatusDto> convertDateStatus(OrderEntity orderEntity) {
        List<OrderDateStatusDto> orderDateStatusDtoList = new ArrayList<>();
        switch (orderEntity.getOrderStatus()) {
            case ORDER_STATUS_DEVLIVERED,ORDER_STATUS_LOADED, ORDER_STATUS_CREATED -> {
                orderDateStatusDtoList.add(
                        createDateStatusDto(OrderStatus.ORDER_STATUS_CREATED, orderEntity.getCreatedDate()));
                orderDateStatusDtoList.add(
                        createDateStatusDto(OrderStatus.ORDER_STATUS_LOADED, orderEntity.getLoadedDate()));
                orderDateStatusDtoList.add(
                        createDateStatusDto(OrderStatus.ORDER_STATUS_DEVLIVERED, orderEntity.getDeliveredDate()));
            }

            case ORDER_STATUS_CANCELLED -> {
                orderDateStatusDtoList.add(
                        createDateStatusDto(OrderStatus.ORDER_STATUS_CREATED, orderEntity.getCreatedDate()));
                orderDateStatusDtoList.add(
                        createDateStatusDto(OrderStatus.ORDER_STATUS_CANCELLED, orderEntity.getCancellationDate()));
            }
        }
        return orderDateStatusDtoList;
    }

    private OrderDateStatusDto createDateStatusDto(OrderStatus orderStatus, Date date) {
        return new OrderDateStatusDto((long) orderStatus.ordinal(), orderStatus.getName(), date);
    }

    @Override
    public CartEntity convertDtoToCartEntity(@NonNull CartDto cartDto) throws BadRequestException {
        GasMaster gasMaster = validationService.validateGasMaster(cartDto.getGasId());
        CartEntity cartEntity = new CartEntity();
        cartEntity.setId(cartDto.getId());
        cartEntity.setCylinderType(CylinderType.getByStatus(cartDto.getCylinderType()));
        cartEntity.setQuantity(cartDto.getQuantity());
        cartEntity.setUserId(cartDto.getUserId());
        cartEntity.setAdminId(cartDto.getAdminId());
        cartEntity.setGasMaster(gasMaster);
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
            cartDto.setPrice(cartEntity.getPrice());
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
    public CylinderEntity convertDtoToCylinderEntity(UserEntity entity, CylinderDto cylinderDto, String requestedRole) throws BadRequestException {
        ResourceCentreEntity resourceCentreEntity = null;
        if (!requestedRole.equals(UserRole.USER_ROLE_USER.getRole())) {
            resourceCentreEntity = validationService.validateResourceCentreEntity(cylinderDto.getResourceCentreId());
        }
        CylinderEntity cylinderEntity = null;
        if (!inventoryRepo.checkIfCylinderCodeExist(cylinderDto.getCylinderCode()).isPresent()) {
            cylinderEntity = new CylinderEntity();
            CylinderStatus cylinderStatus = CylinderStatus.getByStatus(cylinderDto.getStatus());
            cylinderEntity.setCode(cylinderDto.getCylinderCode());
            cylinderEntity.setCylinderStatus(cylinderStatus);
            cylinderEntity.setManufacturer(cylinderDto.getManufacturer());
            if (!ObjectUtils.isEmpty(cylinderDto.getManufacturingDate())) {
                cylinderEntity.setManufacturingDate(new Date(Long.parseLong(cylinderDto.getManufacturingDate())));
            }
            if (!ObjectUtils.isEmpty(cylinderDto.getExpiryDate())) {
                cylinderEntity.setExpiryDate(new Date(Long.parseLong(cylinderDto.getExpiryDate())));
            }
            if (!ObjectUtils.isEmpty(cylinderDto.getLastService())) {
                cylinderEntity.setLastService(new Date(Long.parseLong(cylinderDto.getLastService())));
            }
            if (!ObjectUtils.isEmpty(cylinderDto.getNextService())) {
                cylinderEntity.setNextService(new Date(Long.parseLong(cylinderDto.getNextService())));
            }
            CylinderInventoryDetailsEntity cylinderInventoryDetailsEntity = new CylinderInventoryDetailsEntity();
            cylinderInventoryDetailsEntity.setInventoryStatus(InventoryStatus.INVENTORY_STATUS_IN);
            cylinderInventoryDetailsEntity.setResourceCentreEntity(resourceCentreEntity);
            cylinderEntity.setCylinderInventoryDetailsEntity(cylinderInventoryDetailsEntity);
            cylinderEntity.setUserEntity(entity);
        }
        return cylinderEntity;
    }

    @Override
    public List<OrderEntity> convertCartToOrderEntity(List<CartEntity> cartEntityList, Long addressId) throws BadRequestException {
        List<OrderEntity> orderEntityList = new ArrayList<>();
        for (CartEntity cartEntity : cartEntityList) {
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setCylinderType(cartEntity.getCylinderType());
            orderEntity.setUserId(cartEntity.getUserId());
            orderEntity.setAdminId(cartEntity.getAdminId());
            orderEntity.setOrderStatus(OrderStatus.ORDER_STATUS_CREATED);
            orderEntity.setGasMaster(cartEntity.getGasMaster());
            orderEntity.setPrice(cartEntity.getPrice());
            orderEntity.setRefill(cartEntity.isRefill());
            orderEntity.setRefillCount(cartEntity.getRefillCount());
            orderEntity.setQuantity(cartEntity.getQuantity());
            orderEntity.setAddressEntity(validationService.validateAddressEntity(addressId));
            orderEntity.setOrderNumber("REEK_ORD_" + String.format("%05d", generateRandomOrderNumber()));
            orderEntityList.add(orderEntity);
        }
        return orderEntityList;
    }

    @Override
    public OrderEntity changeOrderStatus(OrderEntity orderEntity, OrderStatus orderStatus, Long deliveryVehicleId) {
        log.info("GenericService >> Changes Status: {} for User: {}", orderStatus.getName(), orderEntity.getUserId());
        switch (orderStatus) {

            case ORDER_STATUS_LOADED -> {
                orderEntity.setLoadedDate(new Date());
                orderEntity.setDeliveryVehicle(deliveryVehicleRepo.getVehicleById(deliveryVehicleId));
            }

            case ORDER_STATUS_DEVLIVERED -> orderEntity.setDeliveredDate(new Date());


            case ORDER_STATUS_CANCELLED -> {
                orderEntity.setActiveFlag(false);
                orderEntity.setCancellationDate(new Date());
            }
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
                .toList();
    }

    @Secured("ROLE_ADMIN")
    @Override
    public UserEntity getUserAdminDetails() throws BadRequestException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepo.findByMobileNumber(Long.parseLong((String) auth.getPrincipal())).orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_REGISTER_USER));
    }

    @Override
    public UserEntity getUserAndCheckUserAdmin(Long userId, Long adminId) throws BadRequestException {
        UserEntity userEntity = validationService.validateUserEntity(userId);
        if (!userEntity.getAdminIdSet().contains(adminId)) {
            throw new BadRequestException(ExceptionConstants.INVALID_USER_ADMIN);
        }
        return userEntity;
    }

    @Override
    public Set<UserDashboardDto> convertGasMappingToDashboardDto(List<AdminGasMapping> adminGasMappingList) {
        return adminGasMappingList.stream().map(e -> new UserDashboardDto(null, e.getCategoryId(), e.getCategoryName())).collect(Collectors.toSet());
    }

    @Override
    public Set<PrivilegeDto> convertToPrivilegeDto(Set<UserPrivilegeEntity> userPrivilegeEntitySet) throws BadRequestException {
        return Arrays.stream(UserPrivilege.values()).map(e -> new PrivilegeDto(e.getName(),
                userPrivilegeEntitySet.stream().anyMatch(p -> p.getPrivilege().equals(e.getPrivilege())))).collect(Collectors.toSet());
    }

    /**
     * Update Existing User With Admin
     *
     * @param entity
     * @param adminId
     * @return
     */
    @Override
    public UserEntity updateUserAdminForUser(UserEntity entity, Long adminId) {
        Set<Long> adminIds = entity.getAdminIdSet();
        adminIds.add(adminId);
        entity.setAdminIdSet(adminIds);
        return entity;
    }
}
