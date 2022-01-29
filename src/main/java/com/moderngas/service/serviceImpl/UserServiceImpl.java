package com.moderngas.service.serviceImpl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.moderngas.constants.Constants;
import com.moderngas.constants.ExceptionConstants;
import com.moderngas.enums.UserRole;
import com.moderngas.exception.BadRequestException;
import com.moderngas.jpaentity.*;
import com.moderngas.pojo.CylinderTypeDto;
import com.moderngas.pojo.NameIdDto;
import com.moderngas.pojo.admin.DeliveryVehicleDto;
import com.moderngas.pojo.admin.UserDetails;
import com.moderngas.pojo.user.*;
import com.moderngas.repository.*;
import com.moderngas.security.JwtProperties;
import com.moderngas.service.EmailService;
import com.moderngas.service.GenericService;
import com.moderngas.service.UserService;
import com.moderngas.service.ValidationService;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

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
    private DeliveryVehicleRepo deliveryVehicleRepo;

    @Autowired
    private InventoryRepo inventoryRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ValidationService validationService;


    @Override
    public String addUser(UserEntityDto userEntityDto) throws BadRequestException {
        log.info("UserService >> Create New User");
        /* Add new Client to DataBase */
        String response = Constants.FAILURE_STR;
        UserEntity userEntity = genericService.convertDtoToUserData(userEntityDto);
        userEntity = userRepo.save(userEntity);
        if (userEntity.getId() != null) {
            response = Constants.SUCCESS_STR;
        }
        return response;
    }

    public String updateUser(UserEntity userEntity) {
        log.info("UserService >> Update User");
        String response = Constants.FAILURE_STR;
        Optional<UserEntity> user = userRepo.findByMobileNumber(userEntity.getMobileNumber());
        if (user.isPresent()) {
            UserEntity tempUser = user.get();
            tempUser.setName(userEntity.getName());
            tempUser.setEmail(userEntity.getEmail());
            tempUser.setCompanyName(userEntity.getCompanyName());

            userRepo.save(tempUser);
            response = Constants.SUCCESS_STR;
        }
        return response;
    }

    @Override
    public Page<UserSearchDto> getAllUserByAdmin(Pageable pageable, String search, Long adminId) throws BadRequestException {
        UserEntity adminEntity = validationService.validateUserEntity(adminId);
        return userRepo.getAllUserByAdmin(pageable, search, adminEntity.getId(), UserRole.USER_ROLE_USER.getRole());
    }

    @Override
    public UserEntityDto getUserById(Long userId) throws BadRequestException {
        UserEntity userEntity = validationService.validateUserEntity(userId);
        return genericService.convertUserDataToDto(userEntity);
    }

    @Override
    public String checkUserExist(Long mobileNumber) {
        String result = Constants.FAILURE_STR;
        Optional<UserEntity> userEntity = userRepo.findByMobileNumber(mobileNumber);
        if (userEntity.isPresent()) {
            result = Constants.SUCCESS_STR;
        }
        return result;
    }

    @Override
    public UserEntity getUserByLoginId(Long username) throws BadRequestException {
        return userRepo.findByMobileNumber(username).orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_USER_NAME));
    }

    @Override
    public String changePassword(Long username, String newPassword) throws BadRequestException {
        log.info("UserService >> Changes password for User: {}", username);
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
                String tempPassword = genericService.generateRandomPassword();

                /* Send forget password mail */
                String subject = "Forget Password..?";
                emailService.sendMail(userEntity.getEmail(), subject, createEmailBody(userEntity.getName(), tempPassword));

                /* Update user with random password */
                userEntity.setPassword(passwordEncoder.encode(tempPassword));
                userEntity.setForgetPassword(true);
                userRepo.save(userEntity);
                result = Constants.SUCCESS_STR;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String createEmailBody(String name, String tempPassword) {
        return "Hi " + name + ", <Br>" + "Have you forget your password to Modern Gas App, Don't worry we have provided a temporary password below, " +
                "<Br><Br>Password : <Strong>" + tempPassword + "</Strong>" +
                "<Br>Now you may directly login to Modern Gas Account with temporary password. " +
                "<Br><Br>Thanks & Regards, <Br> A.B. Chaudhary";
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
        UserEntity adminEntity = validationService.validateUserEntity(adminId);
        List<AdminGasMapping> adminGasMappingList = adminGasMappingRepo.getGasMappingList(adminEntity.getId());
        List<GasDto> gasDtoList = new ArrayList<>();
        for (AdminGasMapping adminGasMapping : adminGasMappingList) {
            GasDto gasDto = new GasDto();
            gasDto.setId(adminGasMapping.getGasId());
            gasDto.setName(adminGasMapping.getGasName());
            gasDto.setCategory(adminGasMapping.getCategoryName());
            gasDto.setAvailable(adminGasMapping.isActiveFlag());
            gasDto.setPrice(adminGasMapping.getPrice());
            gasDto.setAvailableCylinderType(adminGasMapping.getAdminGasCylinderTypeMapping()
                    .stream().map(e -> new CylinderTypeDto(e.getCylinderType().getName(), e.getCylinderType().getDescription())).toList());
            gasDtoList.add(gasDto);
        }
        return gasDtoList;
    }

    @Override
    public String addOrUpdateAddress(AddressDto addressDto, Long userId) throws BadRequestException {
        UserEntity userEntity = validationService.validateUserEntity(userId);
        AddressEntity addressEntity = new AddressEntity();
        if (null != addressDto.getId()) {
            addressEntity = validationService.validateAddressEntity(addressEntity.getId());
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
        String token = JWT.create()
                .withSubject(userEntity.getMobileNumber().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()));

        Set<UserTokenEntity> userTokenSet = userEntity.getUserTokenSet()
                .stream().filter(t -> t.getExpiredDate().after(new Date())).collect(Collectors.toSet());
        UserTokenEntity tokenEntity = new UserTokenEntity();
        tokenEntity.setToken(token);
        tokenEntity.setExpiredDate(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME));
        userTokenSet.add(tokenEntity);
        userEntity.setUserTokenSet(userTokenSet);
        userRepo.save(userEntity);
        return token;
    }

    @Override
    public String logout(String token) throws BadRequestException {
        UserEntity userEntity = userRepo.getUserDetailsByToken(token.replace(JwtProperties.TOKEN_PREFIX, ""));
        if (null == userEntity) {
            throw new BadRequestException(ExceptionConstants.INVALID_USER_TOKEN);
        }
        Set<UserTokenEntity> userTokenSet = userEntity.getUserTokenSet()
                .stream().filter(t -> t.getExpiredDate().after(new Date())).collect(Collectors.toSet());
        userEntity.setUserTokenSet(userTokenSet);
        userRepo.save(userEntity);
        return Constants.SUCCESS_STR;
    }

    @Override
    public GasDto getGasDetailsById(Long id, Long adminId) throws BadRequestException {
        AdminGasMapping adminGasMapping = adminGasMappingRepo.getGasMappingByAdminId(id, adminId)
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.ADMIN_GAS_IS_EMPTY));
        GasMaster gasMaster = validationService.validateGasMaster(id);
        GasDto gasDto = new GasDto();
        gasDto.setCategory(gasMaster.getCategoryMaster().getName());
        gasDto.setId(adminGasMapping.getGasId());
        gasDto.setName(adminGasMapping.getGasName());
        gasDto.setAvailableCylinderType(adminGasMapping.getAdminGasCylinderTypeMapping()
                .stream().map(e -> new CylinderTypeDto(e.getCylinderType().getName(), e.getCylinderType().getDescription()))
                .toList());
        gasDto.setDescription(adminGasMapping.getDescription());
        gasDto.setPrice(adminGasMapping.getPrice());
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
        validationService.validateUserEntity(deliveryVehicleDto.getUserId());
        List<DeliveryVehicleEntity> deliveryVehicleEntityList = genericService.convertDtoToDeliveryVehicle(deliveryVehicleDto);
        deliveryVehicleRepo.saveAll(deliveryVehicleEntityList);
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


}
