package com.moderngas.service.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moderngas.constants.Constants;
import com.moderngas.constants.ExceptionConstants;
import com.moderngas.enums.CylinderType;
import com.moderngas.enums.OrderStatus;
import com.moderngas.exception.BadRequestException;
import com.moderngas.jpaentity.CartEntity;
import com.moderngas.jpaentity.DeliveryVehicle;
import com.moderngas.jpaentity.OrderEntity;
import com.moderngas.jpaentity.UserEntity;
import com.moderngas.pojo.user.CartDto;
import com.moderngas.pojo.user.OrderDto;
import com.moderngas.repository.CartRepo;
import com.moderngas.repository.DeliveryVehicleRepo;
import com.moderngas.repository.GasRepo;
import com.moderngas.repository.OrderRepo;
import com.moderngas.repository.UserRepo;
import com.moderngas.service.GenericService;
import com.moderngas.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public String placeOrder(OrderDto orderDto) {
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
    public List<OrderDto> getOrderListByUser(Long userId) throws BadRequestException {
        UserEntity userEntity = userRepo.findById(userId)
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_USER));
        List<OrderDto> orderDtoList = new ArrayList<>();
        List<OrderEntity> orderEntityList = orderRepo.getOrderEntitiesByUserId(userEntity.getId());
        if (!CollectionUtils.isEmpty(orderEntityList)) {
            orderDtoList = orderEntityList.stream()
                    .map(e -> genericService.convertOrderEntityToDto(e)).collect(Collectors.toList());
        }
        return orderDtoList;
    }

    @Override
    public String addCart(CartDto cartDto) {
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
    public List<CartDto> getCartByUser(Long userId) throws BadRequestException {
        UserEntity userEntity = userRepo.findById(userId)
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_USER));
        List<CartDto> cartDtoList = new ArrayList<>();
        List<CartEntity> cartEntityList = cartRepo.getCartEntitiesByUserIdOrderByUpdatedDate(userEntity.getId());
        if (!CollectionUtils.isEmpty(cartEntityList)) {
            cartDtoList = cartEntityList.stream()
                    .map(e -> genericService.convertCartEntityToDto(e)).collect(Collectors.toList());
        }
        return cartDtoList;
    }

    @Override
    public String deleteOrder(Long orderId) throws BadRequestException {
        log.info("OrderService >> Delete Order {}", orderId);
        OrderEntity orderEntity = orderRepo.findById(orderId)
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_ORDER));
        orderRepo.deleteOrderById(orderEntity.getId());
        return Constants.SUCCESS_STR;
    }

    @Override
    public String deleteCart(Long cartId) throws BadRequestException {
        log.info("OrderService >> Delete Cart {}", cartId);
        CartEntity cartEntity = cartRepo.findById(cartId).orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_CART));
        cartRepo.deleteById(cartEntity.getId());
        return Constants.SUCCESS_STR;
    }

    @Override
    public String placeOrderFromCart(Long userId) throws BadRequestException {
        log.info("OrderService >> Place Order From Cart By User: {}", userId);
        UserEntity userEntity = userRepo.findById(userId)
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_USER));
        String response = Constants.FAILURE_STR;
        List<CartEntity> cartEntityList = cartRepo.getCartEntitiesByUserIdOrderByUpdatedDate(userEntity.getId());
        if (!CollectionUtils.isEmpty(cartEntityList)) {
            List<OrderEntity> orderEntityList = genericService.convertCartToOrderEntity(cartEntityList);
            orderRepo.saveAll(orderEntityList);
            cartRepo.deleteByUserId(userId);
            response = Constants.SUCCESS_STR;
        }
        return response;
    }

    @Override
    public OrderDto getOrderDetailsById(Long orderId) throws BadRequestException {
        OrderEntity orderEntity = orderRepo.findById(orderId)
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_ORDER));
        UserEntity userEntity = userRepo.findById(orderEntity.getUserId())
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_USER));
        OrderDto orderDto = genericService.convertOrderEntityToDto(orderEntity);
        orderDto.setAddressDto(genericService.convertAddressEntityToDto(userEntity.getAddressEntity()));
        orderDto.setUserName(userEntity.getName());
        if (null != orderEntity.getDeliveryVehicleNumber()) {
            Optional<DeliveryVehicle> deliveryVehicle = deliveryVehicleRepo.findById(orderEntity.getDeliveryVehicleNumber());
            orderDto.setDeliveryVehicle(deliveryVehicle.isPresent() ? deliveryVehicle.get().getNumber() : "");
        }
        return orderDto;
    }

    @Override
    public String updateOrderStatus(Long orderId, String status, Long deliveryVehicleId) throws BadRequestException {
        log.info("OrderService >>");
        OrderEntity orderEntity = orderRepo.findById(orderId)
                .orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_ORDER));
        OrderStatus orderStatus = validateOrderStatus(status);
        String response = Constants.FAILURE_STR;
        if (null != orderEntity) {
            orderEntity = genericService.changeOrderStatus(orderEntity, orderStatus, deliveryVehicleId);
            orderRepo.save(orderEntity);
            response = Constants.SUCCESS_STR;
        }
        return response;
    }

    @Override
    public Page<com.moderngas.pojo.admin.OrderDto> getAllOrderListForAdmin(Pageable pageable, String status, List<String> cylinderType, Long adminId, String search, String quantityOrder) throws JsonProcessingException {
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
                adminId, search, quantityOrder);
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
        UserEntity userEntity = userRepo.findById(userId).orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_ORDER));
        return orderRepo.getUserOrderListForAdminInUserDetails(userEntity.getId());
    }
}