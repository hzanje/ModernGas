package com.moderngas.service.serviceImpl;

import com.moderngas.jpaentity.AddressEntity;
import com.moderngas.jpaentity.CategoryMaster;
import com.moderngas.jpaentity.GasImageEntity;
import com.moderngas.jpaentity.GasMaster;
import com.moderngas.jpaentity.UserEntity;
import com.moderngas.pojo.GasDto;
import com.moderngas.pojo.NameIdDto;
import com.moderngas.pojo.UserDashboardDto;
import com.moderngas.pojo.UserEntityDto;
import com.moderngas.repository.GasRepo;
import com.moderngas.repository.UserRepo;
import com.moderngas.service.EmailService;
import com.moderngas.service.GenericService;
import com.moderngas.service.UserService;

import net.minidev.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private static final String FAILURE_STR = "Failure";

    private static final String SUCCESS_STR = "Success";

    @Override
    public String addUser(UserEntityDto userEntityDto) {
        /* Add new Client to DataBase */
        String response = FAILURE_STR;
        UserEntity userEntity = genericService.convertDtoToUserData(userEntityDto);
        userEntity.setActiveFlag(true);
        userEntity.setCreatedDate(new Date());
        userEntity.setUpdatedDate(new Date());
        userEntity = userRepo.save(userEntity);
        if (userEntity.getId() != null) {
            response = SUCCESS_STR;
        }
        return response;
    }

    public String updateUser(UserEntity userEntity) {
        String response = FAILURE_STR;
        Optional<UserEntity> user=userRepo.findByMobileNumber(userEntity.getMobileNumber());
        if(user.isPresent()) {
        	UserEntity tempUser=user.get();
        	tempUser.setUpdatedDate(new Date());
        	tempUser.setName(userEntity.getName());
        	tempUser.setEmail(userEntity.getEmail());
        	tempUser.setCompanyName(userEntity.getCompanyName());
            userRepo.save(tempUser);
            response = SUCCESS_STR;
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
    public UserEntityDto getUserById(Long userId) {
        UserEntity userEntity = userRepo.getOne(userId);
        return genericService.convertUserDataToDto(userEntity);
    }

    @Override
    public String checkUserExist(Long mobileNumber) {
        String result = FAILURE_STR;
        Optional<UserEntity> userEntity = userRepo.findByMobileNumber(mobileNumber);
        if (userEntity.isPresent()) {
            result = SUCCESS_STR;
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
        String result = FAILURE_STR;
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
    public String forgetPassword(Long userName) {
        String result = FAILURE_STR;
        /* Check if User Exits */
        Optional<UserEntity> entity=userRepo.findByMobileNumber(userName);
        if(entity.isPresent()) {
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
        }else {
        	result="User does not exist";
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
    public List<NameIdDto> getListByCategoryId(Long id) {
        List<NameIdDto> nameIdDtoList = new ArrayList<>();
        List<GasMaster> gasMasterList = gasRepo.getGasMasterByCategoryMaster_Id(id);
        if (!CollectionUtils.isEmpty(gasMasterList)) {
            for (GasMaster gasMaster : gasMasterList) {
                NameIdDto nameIdDto = new NameIdDto();
                nameIdDto.setId(gasMaster.getId());
                nameIdDto.setName(gasMaster.getName());
                nameIdDtoList.add(nameIdDto);
            }
        }
        return nameIdDtoList;
    }

    @Override
    public String updateAddress(AddressEntity addressEntity, Long userId) {
        String response = "FAILURE_STR";
        Optional<UserEntity> optionalUser = userRepo.findById(userId);
        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            userEntity.setAddressEntity(addressEntity);
            userRepo.save(userEntity);
            response = SUCCESS_STR;
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
    public GasDto getGasDetailsById(Long id, Long userId) {
        GasMaster gasMaster = gasRepo.getOne(id);
        GasDto gasDto = new GasDto();
        gasDto.setId(gasMaster.getId());
        gasDto.setName(gasMaster.getName());
        gasDto.setAvailableCylinderType(gasMaster.getCylinderTypeMasterList());
        gasDto.setDescription(gasMaster.getDescription());
        gasDto.setPrice(gasMaster.getPrice());
        gasDto.setAvailable(gasMaster.isAvaliable());
        if (!CollectionUtils.isEmpty(gasMaster.getGasImageEntityList())) {
            gasDto.setImageList(gasMaster.getGasImageEntityList().stream()
                    .map(GasImageEntity::getImageUrl).collect(Collectors.toList()));
        }
        return gasDto;
    }
}
