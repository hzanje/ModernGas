package com.moderngas.service.serviceImpl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.moderngas.jpaentity.*;
import com.moderngas.pojo.CylinderTypeDto;
import com.moderngas.repository.*;
import com.moderngas.security.JwtProperties;
import com.moderngas.constants.Constants;
import com.moderngas.constants.ExceptionConstants;
import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.admin.DeliveryVehicleDto;
import com.moderngas.pojo.admin.UserDetails;
import com.moderngas.pojo.user.GasDto;
import com.moderngas.pojo.NameIdDto;
import com.moderngas.pojo.user.UserDashboardDto;
import com.moderngas.pojo.user.UserEntityDto;
import com.moderngas.pojo.user.UserSearchDto;
import com.moderngas.service.EmailService;
import com.moderngas.service.GenericService;
import com.moderngas.service.UserService;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

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
        Optional<UserEntity> user=userRepo.findByMobileNumber(userEntity.getMobileNumber());
        if(user.isPresent()) {
        	UserEntity tempUser=user.get();
        	tempUser.setName(userEntity.getName());
        	tempUser.setEmail(userEntity.getEmail());
        	tempUser.setCompanyName(userEntity.getCompanyName());

            userRepo.save(tempUser);
            response = Constants.SUCCESS_STR;
        }
        return response;
    }

    @Override
    public List<UserEntityDto> getAllUserByAdmin(Long adminId) throws BadRequestException {
        List<UserEntityDto> userEntityDtoList = new ArrayList<>();
        if (null != adminId) {
            List<UserEntity> userEntityList = userRepo.getAllUserByAdmin(adminId);
            for (UserEntity userEntity : userEntityList) {
                UserEntityDto userEntityDto = genericService.convertUserDataToDto(userEntity);
                userEntityDtoList.add(userEntityDto);
            }
        }
        return userEntityDtoList;
    }

    @Override
    public UserEntityDto getUserById(Long userId) throws BadRequestException {
        UserEntity userEntity = userRepo.findById(userId)
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_USER));
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
        UserEntity userEntity =userRepo.findByMobileNumber(userName)
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
        StringBuilder stringBuilder = new StringBuilder("Hi " + name + ", <Br>");
        stringBuilder.append("Have you forget your password to Modern Gas App, Don't worry we have provided a temporary password below, ");
        stringBuilder.append("<Br><Br>Password : <Strong>" + tempPassword + "</Strong>");
        stringBuilder.append("<Br>Now you may directly login to Modern Gas Account with temporary password. ");
        stringBuilder.append("<Br><Br>Thanks & Regards, <Br> A.B. Chaudhary");
        return stringBuilder.toString();
    }

    @Override
    public List<UserDashboardDto> getUserDashboard(Long userId, Long adminId) throws BadRequestException {
        UserEntity userEntity = userRepo.findById(userId)
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_USER));
        if (!userEntity.getAdminIdSet().contains(adminId)) {
            throw new BadRequestException(ExceptionConstants.INVALID_USER_ADMIN);
        }
        List<UserDashboardDto> userDashboardDtoList = new ArrayList<>();


        /* Get all Category*/
        List<CategoryMaster> categoryMasterList = gasRepo.getAllCategory();
        userDashboardDtoList.addAll(genericService.convertCategoryToDto(categoryMasterList));

        /* Get Dashboard Gas  */
        GasMaster gasMaster = gasRepo.getGasMasterByNameEquals("Medical Oxygen");
        if(gasMaster!=null) {
        UserDashboardDto userDashboardDto = new UserDashboardDto();
        userDashboardDto.setId(gasMaster.getId());
        userDashboardDto.setName(gasMaster.getName());
        userDashboardDtoList.add(userDashboardDto);
        }
        return userDashboardDtoList;
    }

    @Override
    public List<NameIdDto> getGasListByCategoryId(Long categoryId) {
        return gasRepo.getGasMasterByCategoryId(categoryId);
    }

    @Override
    public String updateAddress(AddressEntity addressEntity, Long userId) throws BadRequestException {
        UserEntity userEntity = userRepo.findById(userId)
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_USER));
        Set<AddressEntity> addressEntitySet = userEntity.getAddressEntitySet();
        addressEntitySet.add(addressEntity);
        userEntity.setAddressEntitySet(addressEntitySet);
        userRepo.save(userEntity);
        return Constants.SUCCESS_STR;
    }

	@Override
	public JSONObject getAddress(Long userId) throws BadRequestException {
		UserEntity userEntity = userRepo.findById(userId)
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_USER));
		JSONObject obj=new JSONObject();
        Set<AddressEntity> addressSet = userEntity.getAddressEntitySet();
        if (addressSet==null) {
            obj.put("message", "Address does not exist");
        } else {
            obj.put("address", genericService.convertAddressEntitySetToDto(addressSet));
        }
		return obj;
	}

    @Override
    public String refreshToken(String existingToken) throws BadRequestException {
        UserEntity userEntity = userRepo.getUserDetailsByToken(existingToken.replace(JwtProperties.TOKEN_PREFIX,""));
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
        UserEntity userEntity = userRepo.getUserDetailsByToken(token.replace(JwtProperties.TOKEN_PREFIX,""));
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
        GasDto gasDto = new GasDto();
        gasDto.setId(adminGasMapping.getGasId());
        gasDto.setName(adminGasMapping.getGasName());
        gasDto.setAvailableCylinderType(adminGasMapping.getAdminGasCylinderTypeMapping()
                .stream().map(e -> new CylinderTypeDto(e.getCylinderType().getName(), e.getCylinderType().getDescription()))
                .collect(Collectors.toList()));
        gasDto.setDescription(adminGasMapping.getDescription());
        gasDto.setPrice(adminGasMapping.getPrice());
        gasDto.setAvailable(adminGasMapping.isActiveFlag());
        GasMaster gasMaster = gasRepo.findById(id)
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_GAS));
        if (!CollectionUtils.isEmpty(gasMaster.getGasImageEntityList())) {
            gasDto.setImageList(gasMaster.getGasImageEntityList().stream()
                    .map(GasImageEntity::getImageUrl).collect(Collectors.toList()));
        }
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
        userRepo.findById(deliveryVehicleDto.getUserId())
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_USER));
        List<DeliveryVehicleEntity> deliveryVehicleEntityList = genericService.convertDtoToDeliveryVehicle(deliveryVehicleDto);
        deliveryVehicleRepo.saveAll(deliveryVehicleEntityList);
        return Constants.SUCCESS_STR;
    }

    @Override
    public List<NameIdDto> getVehicleNumberList(Long userId) {
        return deliveryVehicleRepo.getVehicleNumberList(userId);
    }

    @Override
    public Page<UserSearchDto> searchUserByName(Pageable pageable, String name) throws BadRequestException {
        if (StringUtils.isEmpty(name)) {
            throw new BadRequestException(ExceptionConstants.INVALID_REQUEST_DATA);
        }
        return userRepo.searchUserByName(pageable, name);
    }

    @Override
    public UserDetails getUserDetailsForAdmin(Long id) throws BadRequestException {
        UserEntity userEntity = userRepo.findById(id)
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_USER));
        UserDetails userDetails = userRepo.getUserDetailsForAdmin(userEntity.getId());
        userDetails.setAssignedCylinder(getUserInventory(userEntity.getId()));
        userDetails.setTotalOrders(getUserOrdersCount(userEntity.getId()));
        return userDetails;
    }

    private int getUserOrdersCount(Long userId) {
        return orderRepo.getOrderCountByUserId(userId);
    }

    private List<String> getUserInventory(Long userId) {
        return inventoryRepo.getAssignedCylinderByUserId(userId);
    }

}
