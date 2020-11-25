package com.moderngas.service.serviceImpl;

import com.moderngas.jpaentity.AddressEntity;
import com.moderngas.jpaentity.CartEntity;
import com.moderngas.jpaentity.CategoryMaster;
import com.moderngas.jpaentity.OrderEntity;
import com.moderngas.jpaentity.UserEntity;
import com.moderngas.pojo.AddressDto;
import com.moderngas.pojo.CartDto;
import com.moderngas.pojo.OrderDto;
import com.moderngas.pojo.UserDashboardDto;
import com.moderngas.pojo.UserEntityDto;
import com.moderngas.repository.GasRepo;
import com.moderngas.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class GenericServiceImpl implements GenericService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    GasRepo gasRepo;

    @Override
    public UserEntity convertDtoToUserData(UserEntityDto userEntityDto) {
        UserEntity userEntity = null;
        if (null != userEntityDto) {
            userEntity = new UserEntity();
            userEntity.setName(userEntityDto.getName());
            userEntity.setEmail(userEntityDto.getEmail());
            userEntity.setMobileNumber(userEntityDto.getMobileNumber());
            userEntity.setCompanyName(userEntityDto.getCompanyName());
            userEntity.setRole(userEntityDto.getRole());
            userEntity.setContactPerson(userEntityDto.getContactPerson());
            if (null != userEntityDto.getPassword() && !userEntityDto.getPassword().isEmpty()) {
                userEntity.setPassword(encodeUserPassword(userEntityDto.getPassword()));
            }
        }
        return userEntity;
    }

    @Override
    public UserEntityDto convertUserDataToDto(UserEntity userEntity) {
        UserEntityDto userEntityDto = new UserEntityDto();
        userEntityDto.setId(userEntity.getId());
        userEntityDto.setName(userEntity.getName());
        userEntityDto.setEmail(userEntity.getEmail());
        userEntityDto.setMobileNumber(userEntity.getMobileNumber());
        userEntityDto.setCompanyName(userEntity.getCompanyName());
        userEntityDto.setRole(userEntity.getRole());
        userEntityDto.setContactPerson(userEntity.getContactPerson());
        return userEntityDto;
    }

    @Override
    public String encodeUserPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public String generateRandomPassword() {
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
            orderEntity.setActiveFlag(true);
            orderEntity.setCreatedDate(new Date());
            orderEntity.setUpdatedDate(new Date());
            orderEntity.setCylinderType(orderDto.getCylinderType());
            orderEntity.setUserId(orderDto.getUserId());
            orderEntity.setStatusMaster(gasRepo.getStatusById(orderDto.getStatusId()));
            orderEntity.setGasMaster(gasRepo.getOne(orderDto.getGasId()));
            orderEntity.setRefill(orderDto.isRefill());
            orderEntity.setRefillCount(orderDto.getRefillCount());
            orderEntity.setOrderDate(new Date());
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
            orderDto.setCategoryName(orderEntity.getGasMaster().getCategoryMaster().getName());
            orderDto.setQuantity(orderEntity.getQuantity());
            orderDto.setPrice(orderEntity.getPrice());
            orderDto.setCylinderType(orderEntity.getCylinderType());
            orderDto.setRefill(orderEntity.isRefill());
            orderDto.setStatusName(orderEntity.getStatusMaster().getStatus());
            orderDto.setStatusId(orderEntity.getStatusMaster().getId());
            orderDto.setUserId(orderDto.getUserId());
            orderDto.setGasId(orderDto.getGasId());
        }
        return orderDto;
    }

    @Override
    public CartEntity convertDtoToCartEntity(CartDto cartDto) {
        CartEntity cartEntity = null;
        if (null != cartDto) {
            cartEntity = new CartEntity();
            cartEntity.setId(cartDto.getId());
            cartEntity.setActiveFlag(true);
            cartEntity.setCreatedDate(new Date());
            cartEntity.setUpdatedDate(new Date());
            cartEntity.setCylinderType(cartDto.getCylinderType());
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
            cartDto.setCylinderType(cartEntity.getCylinderType());
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
            orderEntity.setActiveFlag(true);
            orderEntity.setCreatedDate(new Date());
            orderEntity.setUpdatedDate(new Date());
            orderEntity.setCylinderType(cartEntity.getCylinderType());
            orderEntity.setUserId(cartEntity.getUserId());
            orderEntity.setStatusMaster(gasRepo.getStatusById(1L));
            orderEntity.setGasMaster(cartEntity.getGasMaster());
            orderEntity.setRefill(cartEntity.isRefill());
            orderEntity.setRefillCount(cartEntity.getRefillCount());
            orderEntity.setQuantity(cartEntity.getQuantity());
            orderEntity.setOrderDate(new Date());
            orderEntityList.add(orderEntity);
        }
        return orderEntityList;
    }
}
