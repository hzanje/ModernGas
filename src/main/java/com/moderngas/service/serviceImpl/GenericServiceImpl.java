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
import com.moderngas.pojo.user.AddressDto;
import com.moderngas.pojo.user.CartDto;
import com.moderngas.pojo.user.OrderDto;
import com.moderngas.pojo.user.UserDashboardDto;
import com.moderngas.pojo.user.UserEntityDto;
import com.moderngas.repository.GasRepo;
import com.moderngas.repository.UserRepo;
import com.moderngas.service.GenericService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GenericServiceImpl implements GenericService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GasRepo gasRepo;

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserEntity convertDtoToUserData(UserEntityDto userEntityDto) {
        UserEntity userEntity = null;
        if (null != userEntityDto) {
            userEntity = new UserEntity();
            userEntity.setName(userEntityDto.getName());
            userEntity.setEmail(userEntityDto.getEmail());
            userEntity.setMobileNumber(userEntityDto.getMobileNumber());
            userEntity.setCompanyName(userEntityDto.getCompanyName());
            userEntity.setRoleEntitySet(addUserRole(userEntityDto.getRole()));
            userEntity.setContactPerson(userEntityDto.getContactPerson());
            if (null != userEntityDto.getPassword() && !userEntityDto.getPassword().isEmpty()) {
                userEntity.setPassword(encodeUserPassword(userEntityDto.getPassword()));
            }
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
        userEntity.setRoleEntitySet(addUserRole(adminEntityDto.getRoles()));
        userEntity.setContactPerson(adminEntityDto.getContactPerson());
        userEntity.setAdminGasMappings(gasMappingByNameAndType(adminEntityDto.getGasNameCylinderTypes()));
        return userEntity;
    }

    private Set<AdminGasMapping> gasMappingByNameAndType(List<GasNameCylinderTypeDto> gasNameCylinderTypes) {
        if (CollectionUtils.isEmpty(gasNameCylinderTypes)) {
            return null;
        }
        Set<AdminGasMapping> adminGasMappingSet = new HashSet<>();
        List<GasMaster> gasMasterList = gasRepo.getGasMasterByIdList(
                gasNameCylinderTypes.stream().map(e -> e.getId()).collect(Collectors.toList()));

        for (GasNameCylinderTypeDto nameType : gasNameCylinderTypes) {
            GasMaster gasMaster = gasMasterList.stream().filter(
                    g -> g.getId().equals(nameType.getId())).findFirst().get();
            AdminGasMapping adminGasMapping  = new AdminGasMapping();
            adminGasMapping.setGasId(gasMaster.getId());
            adminGasMapping.setGasName(gasMaster.getName());
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

    private Set<UserRoleEntity> addUserRole(List<String> roles) {
        if (CollectionUtils.isEmpty(roles)) {
            return null;
        }
        Set<UserRoleEntity> userRoleEntitySet = new HashSet<>();
        for (String role : roles) {
            UserRoleEntity userRole = new UserRoleEntity();
            userRole.setRole(UserRole.getByRole(role).getRole());
            userRoleEntitySet.add(userRole);
        }
        return userRoleEntitySet;
    }

    @Override
    public UserEntityDto convertUserDataToDto(UserEntity userEntity) {
        if (null != userEntity) {
            UserEntityDto userEntityDto = new UserEntityDto();
            userEntityDto.setId(userEntity.getId());
            userEntityDto.setName(userEntity.getName());
            userEntityDto.setEmail(userEntity.getEmail());
            userEntityDto.setMobileNumber(userEntity.getMobileNumber());
            userEntityDto.setCompanyName(userEntity.getCompanyName());
            userEntityDto.setOnboarding(userEntity.isOnboarding());
            userEntityDto.setForgetPassword(userEntity.isForgetPassword());
            userEntityDto.setRole(userEntity.getRoleEntitySet()
                    .stream().map(UserRoleEntity::getRole).collect(Collectors.toList()));
            userEntityDto.setContactPerson(userEntity.getContactPerson());
            return userEntityDto;
        }
        return null;
    }

    @Override
    public String encodeUserPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public String generateRandomPassword() {
        log.info("GenericService >> Generating Random Password");
        String capitalLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String smallLetters = "abcdefghijklmnopqrstuvwxyz";
        String specialCharacters = "!@#$*%";
        String numbers = "1234567890";
        String combinedChars = capitalLetters + smallLetters + specialCharacters + numbers;
        Random random = new Random();
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
        addressEntity.setAddress1(addressDto.getAddress1());
        addressEntity.setAddress2(addressDto.getAddress2());
        addressEntity.setLandmark(addressDto.getLandmark());
        addressEntity.setCity(addressDto.getCity());
        addressEntity.setState(addressDto.getState());
        addressEntity.setPincode(addressDto.getPincode());
        return addressEntity;
    }

    @Override
    public AddressDto convertAddressEntityToDto(AddressEntity addressEntity) {
        AddressDto addressDto = new AddressDto();
        if(addressEntity!=null) {
            addressDto.setId(addressEntity.getId());
            addressDto.setAddress1(addressEntity.getAddress1());
            addressDto.setAddress2(addressEntity.getAddress2());
            addressDto.setLandmark(addressEntity.getLandmark());
            addressDto.setCity(addressEntity.getCity());
            addressDto.setState(addressEntity.getState());
            addressDto.setPincode(addressEntity.getPincode());
        }
        return addressDto;
    }

    @Override
    public List<UserDashboardDto> convertCategoryToDto(List<CategoryMaster> categoryMasterList) {
        List<UserDashboardDto> userDashboardDtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(categoryMasterList)) {
            for (CategoryMaster categoryMaster : categoryMasterList) {
                UserDashboardDto userDashboardDto = new UserDashboardDto();
                userDashboardDto.setId(categoryMaster.getId());
                userDashboardDto.setName(categoryMaster.getName());
                userDashboardDto.setCategory(true);
                userDashboardDtoList.add(userDashboardDto);
            }
        }
        return userDashboardDtoList;
    }

    @Override
    public OrderEntity convertDtoToOrderEntity(OrderDto orderDto) {
        OrderEntity orderEntity = null;
        if (null != orderDto) {
            orderEntity = new OrderEntity();
            orderEntity.setCylinderType(CylinderType.getByStatus(orderDto.getCylinderType()));
            orderEntity.setUserId(orderDto.getUserId());
            orderEntity.setOrderStatus(OrderStatus.getByStatus(orderDto.getStatus()));
            orderEntity.setGasMaster(gasRepo.getOne(orderDto.getGasId()));
            orderEntity.setRefill(orderDto.isRefill());
            orderEntity.setRefillCount(orderDto.getRefillCount());
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
    public CartEntity convertDtoToCartEntity(CartDto cartDto) {
        CartEntity cartEntity = null;
        if (null != cartDto) {
            cartEntity = new CartEntity();
            cartEntity.setId(cartDto.getId());
            cartEntity.setCylinderType(CylinderType.getByStatus(cartDto.getCylinderType()));
            cartEntity.setQuantity(cartDto.getQuantity());
            cartEntity.setUserId(cartDto.getUserId());
            cartEntity.setPrice(cartDto.getPrice());
            cartEntity.setRefill(cartDto.isRefill());
            cartEntity.setGasMaster(gasRepo.getOne(cartDto.getGasId()));
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
            cartDto.setPrice(cartEntity.getPrice());
            cartDto.setRefill(cartEntity.isRefill());
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
        if (null != orderStatus) {
            switch (orderStatus) {

                case ORDER_STATUS_LOADED: orderEntity.setLoadedDate(new Date());
                                          orderEntity.setDeliveryVehicleNumber(deliveryVehicleId);
                    break;

                case ORDER_STATUS_DEVLIVERED: orderEntity.setDeliveredDate(new Date());
                    break;

                case ORDER_STATUS_CANCELLED: orderEntity.setActiveFlag(false);
                                             orderEntity.setCancellationDate(new Date());
                    break;

                default: break;
            }
            orderEntity.setOrderStatus(orderStatus);
        }
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
    public DeliveryVehicle convertDtoToDeliveryVehicle(DeliveryVehicleDto deliveryVehicleDto) {
        DeliveryVehicle deliveryVehicle = new DeliveryVehicle();
        deliveryVehicle.setColor(deliveryVehicleDto.getColor());
        deliveryVehicle.setName(deliveryVehicleDto.getName());
        deliveryVehicle.setNumber(deliveryVehicleDto.getNumber());
        deliveryVehicle.setUserId(deliveryVehicleDto.getUserId());
        return deliveryVehicle;
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
}
