package com.moderngas.service.serviceImpl;

import com.moderngas.constants.Constants;
import com.moderngas.constants.ExceptionConstants;
import com.moderngas.enums.MailSubject;
import com.moderngas.enums.UserRole;
import com.moderngas.exception.BadRequestException;
import com.moderngas.jpaentity.*;
import com.moderngas.pojo.CylinderTypeDto;
import com.moderngas.pojo.NameIdDto;
import com.moderngas.pojo.UserDto;
import com.moderngas.pojo.admin.DeliveryVehicleDto;
import com.moderngas.pojo.admin.UserDetails;
import com.moderngas.pojo.user.*;
import com.moderngas.repository.*;
import com.moderngas.security.AESUtil;
import com.moderngas.security.JwtProperties;
import com.moderngas.service.EmailService;
import com.moderngas.service.GenericService;
import com.moderngas.service.UserService;
import com.moderngas.service.ValidationService;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.mail.MessagingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService {

    private static Logger log = LoggerFactory.getLogger(UserServiceImpl.class.getName());

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private GenericService genericService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private GasRepo gasRepo;

    @Autowired
    private AdminGasMappingRepo adminGasMappingRepo;

    @Autowired
    private UserGasMappingRepo userGasMappingRepo;

    @Autowired
    private DeliveryVehicleRepo deliveryVehicleRepo;

    @Autowired
    private InventoryRepo inventoryRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ValidationService validationService;

    @Override
    public String addUser(Long adminId, UserDto userDto) throws BadRequestException, NoSuchAlgorithmException, MessagingException {
        log.info("UserService :: addUser >>> AdminId : {}", adminId);
        UserEntity adminEntity = validationService.validateAdminEntity(adminId);
        UserEntity userEntity = validationService.checkUserAlreadyExistInSystem(userDto.getMobileNumber(), adminEntity);
        if (ObjectUtils.isEmpty(userEntity)) {
            userEntity = new UserEntity();
        } else if (!userEntity.getAdminIdSet().contains(adminEntity.getId())) {
            userEntity.setAdminIdSet(genericService.addOrUpdateUserAdmin(userEntity, adminEntity.getId()));
            userRepo.save(userEntity);
            return Constants.USER_ALREADY_REGISTER_ASSIGNED;
        }
        if (!ObjectUtils.isEmpty(userDto.getId())) {
            userEntity = validationService.validateUserEntity(userDto.getId());
        }
        userEntity = genericService.convertUserDtoToEntity(userEntity, userDto, adminEntity, UserRole.USER_ROLE_USER);
        userEntity.setAdminIdSet(genericService.addOrUpdateUserAdmin(userEntity, adminEntity.getId()));
        userEntity.setCompanyName(userDto.getCompanyName());
        userEntity.setPassword(genericService.generatePasswordAndSendMail(userEntity, MailSubject.MAIL_SUBJECT_NEW_PASSWORD));
        userRepo.save(userEntity);
        return Constants.SUCCESS_STR;
    }

    @Override
    public String updateUser(Long adminId, UserDto userDto) throws BadRequestException {
        UserEntity adminEntity = validationService.validateAdminEntity(adminId);
        UserEntity userEntity = validationService.validateUserEntity(userDto.getId());
        userEntity = genericService.convertUserDtoToEntity(userEntity, userDto, adminEntity, UserRole.USER_ROLE_USER);
        userEntity.setCompanyName(userDto.getCompanyName());
        if (null != userDto.getPassword() && !userDto.getPassword().isEmpty()) {
            userEntity.setPassword(genericService.encodeUserPassword(userDto.getPassword()));
        }
        userRepo.save(userEntity);
        return Constants.SUCCESS_STR;
    }

    @Override
    public String deleteUser(Long adminId, Long userId) throws BadRequestException {
        UserEntity userEntity = validationService.validateUserEntity(userId);
        UserEntity adminEntity = validationService.validateAdminEntity(adminId);
        Set<Long> userEntityAdminIdSet = userEntity.getAdminIdSet();
        if (userEntityAdminIdSet.remove(adminEntity.getId())) {
            userEntity.setAdminIdSet(userEntityAdminIdSet);
            userRepo.save(userEntity);
        }
        return Constants.SUCCESS_STR;
    }

    @Override
    public Page<UserSearchDto> getAllUserByAdmin(Pageable pageable, String search, Long adminId) throws BadRequestException {
        UserEntity adminEntity = validationService.validateAdminEntity(adminId);
        return userRepo.getAllUserByAdmin(pageable, search, adminEntity.getId(), UserRole.USER_ROLE_USER.getRole());
    }

    @Override
    public UserEntityResponseDto getUserById(Long userId) throws BadRequestException {
        UserEntity userEntity = validationService.validateUserEntity(userId);
        return genericService.convertUserDataToDto(userEntity);
    }

    @Override
    public UserEntity getUserByLoginId(Long username) throws BadRequestException {
        return userRepo.findByMobileNumber(username).orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_USER_NAME));
    }

    @Override
    public String changePassword(Long username, String newPassword) throws BadRequestException {
        log.info("UserService >> Changes password for User: {}", username);
        genericService.checkUserNameAndToken(username);
        UserEntity userEntity = userRepo.findByMobileNumber(username)
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_USER));
        userEntity.setPassword(passwordEncoder.encode(newPassword));
        if (userEntity.isOnboarding()) {
            userEntity.setOnboarding(false);
        }
        if (userEntity.isForgetPassword()) {
            userEntity.setForgetPassword(false);
        }
        userRepo.save(userEntity);
        return Constants.SUCCESS_STR;
    }

    @Override
    public String forgetPassword(Long userName) throws BadRequestException {
        log.info("UserService >> Forget Password by User: {}", userName);
        String result = Constants.FAILURE_STR;
        /* Check if User Exits */
        UserEntity userEntity = userRepo.findByMobileNumber(userName)
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_USER));
        try {
            if (null != userEntity && null != userEntity.getEmail()) {
                String password = genericService.generatePasswordAndSendMail(userEntity, MailSubject.MAIL_SUBJECT_FORGET_PASSWORD);
                /* Update user with random password */
                userEntity.setPassword(password);
                userEntity.setForgetPassword(true);
                userRepo.save(userEntity);
                result = Constants.SUCCESS_STR;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Set<UserDashboardDto> getUserDashboard(Long userId, Long adminId) throws BadRequestException {
        UserEntity userEntity = validationService.validateUserEntity(userId);
        if (!userEntity.getAdminIdSet().contains(adminId)) {
            throw new BadRequestException(ExceptionConstants.INVALID_USER_ADMIN);
        }
        Set<UserDashboardDto> userDashboardSet = new LinkedHashSet();

        /* Get all Category*/
        List<CategoryMaster> categoryMasterList = gasRepo.getAllCategory();
        /* Get Order Category by Admin's selection  */
        List<AdminGasMapping> adminGasMappingList = adminGasMappingRepo.getAllGasMappingByAdminId(adminId);

        userDashboardSet.addAll(genericService.convertGasMappingToDashboardDto(adminGasMappingList));
        userDashboardSet.addAll(genericService.convertCategoryToDto(categoryMasterList, userDashboardSet));
        return userDashboardSet;
    }

    @Override
    public List<GasNameIdDto> getGasListByCategoryId(Long categoryId, Long adminId) {
        List<AdminGasMapping> adminGasMappingList = adminGasMappingRepo.getGasMappingListByCategoryId(categoryId, adminId);
        return adminGasMappingList.stream()
                .map(e -> new GasNameIdDto(e.getGasId(), e.getGasName(), ""))
                .toList();
    }

    @Override
    public List<GasDto> getAllGasList(Long adminId) throws BadRequestException{
        UserEntity adminEntity = validationService.validateAdminEntity(adminId);
        List<AdminGasMapping> adminGasMappingList = adminGasMappingRepo.getGasMappingList(adminEntity.getId());
        List<GasDto> gasDtoList = new ArrayList<>();
        for (AdminGasMapping adminGasMapping : adminGasMappingList) {
            GasDto gasDto = new GasDto();
            gasDto.setId(adminGasMapping.getGasId());
            gasDto.setName(adminGasMapping.getGasName());
            gasDto.setCategory(adminGasMapping.getCategoryName());
            gasDto.setAvailable(adminGasMapping.isActiveFlag());
            gasDto.setAvailableCylinderType(adminGasMapping.getAdminGasCylinderTypeMapping()
                    .stream().map(e -> new CylinderTypeDto(e.getCylinderType().getName(), e.getCylinderType().getDescription(), e.getPrice())).toList());
            gasDtoList.add(gasDto);
        }
        return gasDtoList;
    }

    @Override
    public String addOrUpdateAddress(AddressDto addressDto, Long userId) throws BadRequestException {
        UserEntity userEntity = validationService.validateUserEntity(userId);
        AddressEntity addressEntity = new AddressEntity();
        if (null != addressDto.getId()) {
            addressEntity = validationService.validateAddressEntity(addressDto.getId());
        }
        addressEntity = genericService.convertDtoToAddressEntity(addressDto, addressEntity);
        addressRepo.save(addressEntity);
        Set<AddressEntity> addressEntitySet = userEntity.getAddressEntitySet();
        addressEntitySet.add(addressEntity);
        userEntity.setAddressEntitySet(addressEntitySet);
        userRepo.save(userEntity);
        return Constants.SUCCESS_STR;
    }

    @Override
    public JSONObject getAddress(Long userId) throws BadRequestException {
        UserEntity userEntity = validationService.validateUserEntity(userId);
        JSONObject obj = new JSONObject();
        Set<AddressEntity> addressSet = userEntity.getAddressEntitySet();
        if (addressSet == null) {
            obj.put("message", "Address does not exist");
        } else {
            obj.put("address", genericService.convertAddressEntitySetToDto(addressSet));
        }
        return obj;
    }

    @Override
    public String deleteUserAddress(Long id) {
        addressRepo.deleteById(id);
        return Constants.SUCCESS_STR;
    }

    @Override
    public String refreshToken(String existingToken) throws BadRequestException {
        UserEntity userEntity = userRepo.getUserDetailsByToken(existingToken.replace(JwtProperties.TOKEN_PREFIX, ""));
        if (null == userEntity) {
            throw new BadRequestException(ExceptionConstants.INVALID_USER_TOKEN);
        }
        String token = AESUtil.createJWTToken(userEntity.getMobileNumber().toString());
        Set<UserTokenEntity> updatedTokenSet = updateTokenSetForUser(token, userEntity.getUserTokenSet());
        updatedTokenSet.add(generateTokenEntity(token));
        userEntity.setUserTokenSet(updatedTokenSet);
        userRepo.save(userEntity);
        return token;
    }

    private UserTokenEntity generateTokenEntity(String token) {
        UserTokenEntity tokenEntity = new UserTokenEntity();
        tokenEntity.setToken(token);
        tokenEntity.setExpiredDate(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME));
        return tokenEntity;
    }

    @Override
    public String logout(String token) throws BadRequestException {
        UserEntity userEntity = userRepo.getUserDetailsByToken(token.replace(JwtProperties.TOKEN_PREFIX, ""));
        if (null == userEntity) {
            throw new BadRequestException(ExceptionConstants.INVALID_USER_TOKEN);
        }
        userEntity.setUserTokenSet(updateTokenSetForUser(token, userEntity.getUserTokenSet()));
        userRepo.save(userEntity);
        return Constants.SUCCESS_STR;
    }

    private Set<UserTokenEntity> updateTokenSetForUser(String token, Set<UserTokenEntity> userTokenSet) {
        return userTokenSet.stream().filter(e -> !e.getToken()
                .equals(token.replace(JwtProperties.TOKEN_PREFIX, ""))).collect(Collectors.toSet());
    }

    @Override
    public GasDto getGasDetailsById(Long id, Long adminId, Long userId) throws BadRequestException {
        GasMaster gasMaster = validationService.validateGasMaster(id);
        AdminGasMapping adminGasMapping = adminGasMappingRepo.getGasMappingByAdminId(gasMaster.getId(), adminId)
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.ADMIN_GAS_IS_EMPTY));
        UserGasMapping userGasMapping = userGasMappingRepo.getGasMappingByGasIdAndAdminIdAndUserId(gasMaster.getId(), adminId, userId);
        GasDto gasDto = new GasDto();
        gasDto.setCategory(gasMaster.getCategoryMaster().getName());
        gasDto.setId(adminGasMapping.getGasId());
        gasDto.setName(adminGasMapping.getGasName());
        if (ObjectUtils.isEmpty(userGasMapping)) {
            gasDto.setAvailableCylinderType(adminGasMapping.getAdminGasCylinderTypeMapping()
                    .stream().map(e -> new CylinderTypeDto(e.getCylinderType().getName(), e.getCylinderType().getDescription(), e.getPrice()))
                    .toList());
        } else {
            List<AdminGasCylinderTypeMapping> uncommonMapping = adminGasMapping.getAdminGasCylinderTypeMapping()
                    .stream().filter(a -> userGasMapping.getUserGasCylinderTypeMapping().stream().anyMatch(u -> !u.getCylinderType().getName().equals(a.getCylinderType().getName()))).toList();
            List<CylinderTypeDto> adminCylinderTypeDtoList = userGasMapping.getUserGasCylinderTypeMapping()
                    .stream().map(e -> new CylinderTypeDto(e.getCylinderType().getName(), e.getCylinderType().getDescription(), e.getPrice()))
                    .toList();
            List<CylinderTypeDto> userCylinderTypeDtoList =uncommonMapping.stream().map(e -> new CylinderTypeDto(e.getCylinderType().getName(), e.getCylinderType().getDescription(), e.getPrice())).toList();
            gasDto.setAvailableCylinderType(Stream.concat(adminCylinderTypeDtoList.stream(), userCylinderTypeDtoList.stream()).toList());
        }
        gasDto.setDescription(adminGasMapping.getDescription());
        gasDto.setAvailable(adminGasMapping.isActiveFlag());
        return gasDto;
    }

    @Override
    public void checkIfRoleIsNotUser(UserEntity userEntity) throws BadRequestException {
        if (null == userEntity) {
            throw new BadRequestException(ExceptionConstants.INVALID_USER);
        }
    }

    @Override
    public String addVehicle(DeliveryVehicleDto deliveryVehicleDto) throws BadRequestException {
        if (CollectionUtils.isEmpty(deliveryVehicleDto.getNumbers())) {
            throw new BadRequestException(ExceptionConstants.INVALID_REQUEST_DATA);
        }
        validationService.validateAdminEntity(deliveryVehicleDto.getUserId());
        List<DeliveryVehicleEntity> deliveryVehicleEntityList = genericService.convertDtoToDeliveryVehicle(deliveryVehicleDto);
        deliveryVehicleRepo.saveAll(deliveryVehicleEntityList);
        return Constants.SUCCESS_STR;
    }

    @Override
    public String deleteVehicle(Long vehicleId) throws BadRequestException {
        validationService.validateDeliveryVehicleEntity(vehicleId);
        deliveryVehicleRepo.deleteById(vehicleId);
        return Constants.SUCCESS_STR;
    }

    @Override
    public List<NameIdDto> getVehicleNumberList(Long userId) {
        return deliveryVehicleRepo.getVehicleNumberList(userId);
    }

    @Override
    public UserDetails getUserDetailsForAdmin(Long id) throws BadRequestException {
        UserEntity userEntity = validationService.validateUserEntity(id);
        UserDetails userDetails = userRepo.getUserDetailsForAdmin(userEntity.getId());
        userDetails.setTotalOrders(getUserOrdersCount(userEntity.getId()));
        return userDetails;
    }

    private int getUserOrdersCount(Long userId) {
        return orderRepo.getOrderCountByUserId(userId);
    }

    @Override
    public List<FrequentOrderProductDto> getFrequentlyOrderProduct(Long userId, Long adminId) throws BadRequestException {
        UserEntity userEntity = validationService.validateUserEntity(userId);
        UserEntity adminEntity = validationService.validateAdminEntity(adminId);
        Pageable pageable = PageRequest.of(0, 5);
        return orderRepo.getFrequentlyOrderProduct(pageable ,userEntity.getId(), adminEntity.getId()).getContent();
    }
}
