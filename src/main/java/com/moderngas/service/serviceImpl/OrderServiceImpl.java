package com.moderngas.service.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.moderngas.constants.Constants;
import com.moderngas.constants.ExceptionConstants;
import com.moderngas.enums.CylinderType;
import com.moderngas.enums.OrderStatus;
import com.moderngas.exception.BadRequestException;
import com.moderngas.jpaentity.*;
import com.moderngas.pojo.admin.OpenOrderDto;
import com.moderngas.pojo.user.CartDto;
import com.moderngas.pojo.user.OrderDto;
import com.moderngas.repository.*;
import com.moderngas.service.GenericService;
import com.moderngas.service.OrderService;
import com.moderngas.service.ValidationService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private GenericService genericService;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private GasRepo gasRepo;

    @Autowired
    private DeliveryVehicleRepo deliveryVehicleRepo;

    @Autowired
    private ValidationService validationService;

    @Override
    public String placeOrder(OrderDto orderDto) throws BadRequestException {
        log.info("OrderService >> Place Order By User: {}", orderDto.getUserId());
        String response = Constants.FAILURE_STR;
        OrderEntity orderEntity = genericService.convertDtoToOrderEntity(orderDto);
        if (null != orderEntity) {
            orderRepo.save(orderEntity);
            response = Constants.SUCCESS_STR;
        }
        return response;
    }

    @Override
    public List<OrderDto> getOrderListByUser(Long userId, Long adminId) throws BadRequestException {
        UserEntity userEntity = genericService.getUserAndCheckUserAdmin(userId, adminId);
        List<OrderDto> orderDtoList = new ArrayList<>();
        List<OrderEntity> orderEntityList = orderRepo.getOrderEntitiesByUserId(userEntity.getId(), adminId);
        if (!CollectionUtils.isEmpty(orderEntityList)) {
            orderDtoList = orderEntityList.stream()
                    .map(e -> genericService.convertOrderEntityToDto(e)).toList();
        }
        return orderDtoList;
    }

    @Override
    public String addOrUpdateCart(CartDto cartDto) throws BadRequestException {
        log.info("OrderService >> Add Cart by User: {}", cartDto.getUserId());
        String response = Constants.FAILURE_STR;
        CartEntity cartEntity = genericService.convertDtoToCartEntity(cartDto);
        if (null != cartEntity) {
            cartRepo.save(cartEntity);
            response = Constants.SUCCESS_STR;
        }
        return response;
    }

    @Override
    public List<CartDto> getCartByUser(Long userId, Long adminId) throws BadRequestException {
        UserEntity userEntity = genericService.getUserAndCheckUserAdmin(userId, adminId);
        List<CartDto> cartDtoList = new ArrayList<>();
        List<CartEntity> cartEntityList = cartRepo.getCartEntitiesByUserIdAndAdminIdOrderByUpdatedDate(userEntity.getId(), adminId);
        if (!CollectionUtils.isEmpty(cartEntityList)) {
            cartDtoList = cartEntityList.stream()
                    .map(e -> genericService.convertCartEntityToDto(e)).toList();
        }
        return cartDtoList;
    }

    @Override
    public String deleteOrder(Long orderId) throws BadRequestException {
        log.info("OrderService >> Delete Order {}", orderId);
        OrderEntity orderEntity = validationService.validateOrderEntity(orderId);
        orderRepo.deleteOrderById(orderEntity.getId());
        return Constants.SUCCESS_STR;
    }

    @Override
    public String deleteCart(Long cartId) throws BadRequestException {
        log.info("OrderService >> Delete Cart {}", cartId);
        CartEntity cartEntity = validationService.validateCartEntity(cartId);
        cartRepo.deleteById(cartEntity.getId());
        return Constants.SUCCESS_STR;
    }

    @Override
    public String placeOrderFromCart(Long userId, Long adminId, Long addressId) throws BadRequestException {
        log.info("OrderService >> Place Order From Cart By User: {}", userId);
        UserEntity userEntity = genericService.getUserAndCheckUserAdmin(userId, adminId);
        String response = Constants.FAILURE_STR;
        List<CartEntity> cartEntityList = cartRepo.getCartEntitiesByUserIdAndAdminIdOrderByUpdatedDate(userEntity.getId(), adminId);
        if (!CollectionUtils.isEmpty(cartEntityList)) {
            List<OrderEntity> orderEntityList = genericService.convertCartToOrderEntity(cartEntityList, addressId);
            orderRepo.saveAll(orderEntityList);
            cartRepo.deleteByUserId(userId, adminId);
            response = Constants.SUCCESS_STR;
        }
        return response;
    }

    @Override
    public OrderDto getOrderDetailsById(Long orderId) throws BadRequestException {
        OrderEntity orderEntity = validationService.validateOrderEntity(orderId);
        UserEntity userEntity = validationService.validateUserEntity(orderEntity.getUserId());
        OrderDto orderDto = genericService.convertOrderEntityToDto(orderEntity);
        orderDto.setAddressDto(genericService.convertAddressEntityToDto(orderEntity.getAddressEntity()));
        orderDto.setUserName(userEntity.getName());
        if (null != orderEntity.getDeliveryVehicle()) {
            orderDto.setDeliveryVehicle(orderEntity.getDeliveryVehicle() == null ? "" : orderEntity.getDeliveryVehicle().getName());
        }
        return orderDto;
    }

    @Override
    public String updateOrderStatus(Long orderId, String status, Long deliveryVehicleId) throws BadRequestException {
        log.info("OrderService >>");
        OrderEntity orderEntity = validationService.validateOrderEntity(orderId);
        DeliveryVehicleEntity deliveryVehicleEntity = validationService.validateDeliveryVehicleEntity(deliveryVehicleId);
        OrderStatus orderStatus = validateOrderStatus(status);
        String response = Constants.FAILURE_STR;
        if (null != orderEntity) {
            orderEntity = genericService.changeOrderStatus(orderEntity, orderStatus, deliveryVehicleEntity.getId());
            orderRepo.save(orderEntity);
            response = Constants.SUCCESS_STR;
        }
        return response;
    }

    @Override
    public Page<com.moderngas.pojo.admin.OrderDto> getAllOrderListForAdmin(Pageable pageable, String status, List<String> cylinderType, String search,Long id,  String quantityOrder) throws JsonProcessingException, BadRequestException {
        UserEntity userEntity = validationService.validateUserEntity(id);
        List<OrderStatus> statusList = new ArrayList<>();
        List<CylinderType> cylinderTypeList = new ArrayList<>();
        if (null != status) {
            statusList.add(OrderStatus.getByStatus(status));
        } else {
            statusList = OrderStatus.getOrderStatusList();
        }
        if (!CollectionUtils.isEmpty(cylinderType)) {
            for (String type : cylinderType) {
                cylinderTypeList.add(CylinderType.getByStatus(type));
            }
        } else {
            cylinderTypeList = CylinderType.getCylinderTypeList();
        }
        return orderRepo.getAllOrderListForAdmin(pageable, statusList, cylinderTypeList,
                userEntity.getId(), search, quantityOrder);
    }

    public OrderStatus validateOrderStatus(String status) throws BadRequestException {
        if (!OrderStatus.isExist(status)) {
            throw new BadRequestException(ExceptionConstants.INVALID_STATUS);
        }
        OrderStatus orderStatus = OrderStatus.getByStatus(status);
        if (null == orderStatus) {
            throw new BadRequestException(ExceptionConstants.INVALID_STATUS);
        }
        return orderStatus;
    }

    @Override
    public List<com.moderngas.pojo.admin.OrderDto> getUserOrderListForAdminInUserDetails(Long userId) throws BadRequestException {
        UserEntity userEntity = validationService.validateUserEntity(userId);
        return orderRepo.getUserOrderListForAdminInUserDetails(userEntity.getId());
    }

    @Override
    public String placeAdminInitiatedOrder(@NonNull OpenOrderDto openOrderDto, Long adminId) throws BadRequestException, NoSuchAlgorithmException {
        UserEntity adminEntity = validationService.validateUserEntity(adminId);
        UserEntity userEntity = validationService.validateUserEntity(openOrderDto.getUserId());
        AddressEntity addressEntity = validationService.validateAddressEntity(openOrderDto.getAddressId());
        GasMaster gasMaster = validationService.validateGasMaster(openOrderDto.getProductId());

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCylinderType(CylinderType.getByStatus(openOrderDto.getCylinderType()));
        orderEntity.setUserId(userEntity.getId());
        orderEntity.setAdminId(adminEntity.getId());
        orderEntity.setOrderStatus(OrderStatus.ORDER_STATUS_CREATED);
        orderEntity.setGasMaster(gasMaster);
        orderEntity.setRefill(false);
        orderEntity.setRefillCount(0);
        orderEntity.setAddressEntity(addressEntity);
        orderEntity.setOrderNumber("REEK_ORD_" + String.format("%05d", genericService.generateRandomOrderNumber()));
        orderRepo.save(orderEntity);
        return Constants.SUCCESS_STR;
    }
}