package com.moderngas.service.serviceImpl;

import com.moderngas.constants.Constants;
import com.moderngas.constants.ExceptionConstants;
import com.moderngas.enums.CylinderType;
import com.moderngas.exception.BadRequestException;
import com.moderngas.exception.UnauthorizedException;
import com.moderngas.jpaentity.AddressEntity;
import com.moderngas.jpaentity.CategoryMaster;
import com.moderngas.jpaentity.DeliveryVehicle;
import com.moderngas.jpaentity.GasImageEntity;
import com.moderngas.jpaentity.GasMaster;
import com.moderngas.jpaentity.OrderEntity;
import com.moderngas.jpaentity.UserEntity;
import com.moderngas.jpaentity.UserRoleEntity;
import com.moderngas.pojo.admin.DeliveryVehicleDto;
import com.moderngas.pojo.admin.UserDetails;
import com.moderngas.pojo.user.GasDto;
import com.moderngas.pojo.NameIdDto;
import com.moderngas.pojo.user.UserDashboardDto;
import com.moderngas.pojo.user.UserEntityDto;
import com.moderngas.pojo.user.UserSearchDto;
import com.moderngas.repository.DeliveryVehicleRepo;
import com.moderngas.repository.GasRepo;
import com.moderngas.repository.InventoryRepo;
import com.moderngas.repository.OrderRepo;
import com.moderngas.repository.UserRepo;
import com.moderngas.service.EmailService;
import com.moderngas.service.GenericService;
import com.moderngas.service.UserService;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
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
    private DeliveryVehicleRepo deliveryVehicleRepo;

    @Autowired
    private InventoryRepo inventoryRepo;

    @Autowired
    private OrderRepo orderRepo;


    @Override
    public String addUser(UserEntityDto userEntityDto) {
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
    public List<UserEntityDto> getAllUser() {
        List<UserEntity> userEntityList = userRepo.findAll();
        List<UserEntityDto> userEntityDtoList = new ArrayList<>();
        for (UserEntity userEntity : userEntityList) {
            UserEntityDto userEntityDto = genericService.convertUserDataToDto(userEntity);
            userEntityDtoList.add(userEntityDto);
        }
        return userEntityDtoList;
    }

    @Override
    public UserEntityDto getUserById(Long userId) throws BadRequestException {
        UserEntity userEntity = userRepo.findById(userId).orElse(null);
        if (null == userEntity) {
            throw new BadRequestException(ExceptionConstants.INVALID_USER);
        }
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
    public UserEntity getUserByLoginId(Long username) {
        Optional<UserEntity> userEntity = userRepo.findByMobileNumber(username);
        if (userEntity.isPresent()) {
            return userEntity.get();
        }
        return null;
    }

    @Override
    public String changePassword(Long username, String oldPassword, String newPassword) {
        log.info("UserService >> Changes password for User: {}", username);
        String result = Constants.FAILURE_STR;
        UserEntity userEntity;
        Optional<UserEntity> optionalUserEntity = userRepo.findByMobileNumber(username);
        if (optionalUserEntity.isPresent()) {
            userEntity = optionalUserEntity.get();
            if (passwordEncoder.matches(oldPassword, userEntity.getPassword())) {
                userEntity.setPassword(passwordEncoder.encode(newPassword));
                result = updateUser(userEntity);
            }
        }
        return result;
    }

    @Override
    public String forgetPassword(Long userName) throws BadRequestException {
        log.info("UserService >> Forget Password by User: {}", userName);
        String result = Constants.FAILURE_STR;
        /* Check if User Exits */
        Optional<UserEntity> entity=userRepo.findByMobileNumber(userName);
        if(!entity.isPresent()) {
            throw new BadRequestException(ExceptionConstants.INVALID_USER);
        }
        UserEntity userEntity = entity.get();
        try {
            if (null != userEntity && null != userEntity.getEmail()) {
                String tempPassword = genericService.generateRandomPassword();

                /* Send forget password mail */
                String subject = "Forget Password..?";
                emailService.sendMail(userEntity.getEmail(), subject, createEmailBody(userEntity.getName(), tempPassword));

                /* Update user with random password */
                userEntity.setPassword(passwordEncoder.encode(tempPassword));
                result = updateUser(userEntity);

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
    public List<UserDashboardDto> getUserDashboard(Long userId) {
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
        userDashboardDto.setCategory(false);
        userDashboardDtoList.add(userDashboardDto);
        }
        return userDashboardDtoList;
    }

    @Override
    public List<NameIdDto> getListByCategoryId(Long categoryId) {
        return gasRepo.getGasMasterByCategoryId(categoryId);
    }

    @Override
    public String updateAddress(AddressEntity addressEntity, Long userId) {
        String response = Constants.FAILURE_STR;
        Optional<UserEntity> optionalUser = userRepo.findById(userId);
        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            userEntity.setAddressEntity(addressEntity);
            userRepo.save(userEntity);
            response = Constants.SUCCESS_STR;
        }
        return response;
    }

	@Override
	public JSONObject getAddress(Long userId) {
		Optional<UserEntity> optional=userRepo.findById(userId);
		JSONObject obj=new JSONObject();
		if (optional.isPresent()) {
			AddressEntity address=optional.get().getAddressEntity();
			if (address==null) {
				obj.put("message", "Address does not exist");
			} else {
				obj.put("address", genericService.convertAddressEntityToDto(address));
			}
		} else {
			obj.put("message", "User does not exists");
		}
		return obj;
	}

    @Override
    public String refreshToken(String existingToken) {
        return null;
    }

    @Override
    public GasDto getGasDetailsById(Long id) throws BadRequestException {
        GasMaster gasMaster = gasRepo.findById(id).orElse(null);
        if (null == gasMaster) {
            throw new BadRequestException(ExceptionConstants.INVALID_GAS);
        }
        GasDto gasDto = new GasDto();
        gasDto.setId(gasMaster.getId());
        gasDto.setName(gasMaster.getName());
        gasDto.setAvailableCylinderType(CylinderType.getCylinderTypeDtoList());
        gasDto.setDescription(gasMaster.getDescription());
        gasDto.setPrice(gasMaster.getPrice());
        gasDto.setAvailable(gasMaster.isAvaliable());
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
        } /*else if (userEntity.getRoleEntitySet().contains(UserRoleEntity :: getRole)) {
            throw new UnauthorizedException(ExceptionConstants.INVALID_USER_ACCESS);
        }*/
    }

    @Override
    public String addVehicle(DeliveryVehicleDto deliveryVehicleDto) throws BadRequestException {
        if (null == deliveryVehicleDto) {
            throw new BadRequestException(ExceptionConstants.INVALID_REQUEST_DATA);
        }
        UserEntity userEntity = userRepo.findById(deliveryVehicleDto.getUserId()).orElse(null);
        DeliveryVehicle deliveryVehicle = genericService.convertDtoToDeliveryVehicle(deliveryVehicleDto);
        deliveryVehicleRepo.save(deliveryVehicle);
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
        UserEntity userEntity = userRepo.findById(id).orElse(null);
        if (null == userEntity) {
            throw new BadRequestException(ExceptionConstants.INVALID_USER);
        }
        UserDetails userDetails = userRepo.getUserDetailsForAdmin(id);
        userDetails.setAssignedCylinder(getUserInventory(id));
        userDetails.setTotalOrders(getUserOrdersCount(id));
        return userDetails;
    }

    private int getUserOrdersCount(Long userId) {
        List<OrderEntity> orderEntityList = orderRepo.getOrderEntitiesByUserId(userId);
        if (CollectionUtils.isEmpty(orderEntityList)) {
            return 0;
        }
        return orderEntityList.size();
    }

    private List<String> getUserInventory(Long userId) {
        return inventoryRepo.getAssignedCylinderByUserId(userId);
    }
}
