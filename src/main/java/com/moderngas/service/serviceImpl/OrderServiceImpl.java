package com.moderngas.service.serviceImpl;

import com.moderngas.constants.Constants;
import com.moderngas.constants.ExceptionConstants;
import com.moderngas.enums.CylinderType;
import com.moderngas.enums.OrderStatus;
import com.moderngas.exception.BadRequestException;
import com.moderngas.jpaentity.CartEntity;
import com.moderngas.jpaentity.OrderEntity;
import com.moderngas.jpaentity.UserEntity;
import com.moderngas.pojo.user.CartDto;
import com.moderngas.pojo.user.OrderDto;
import com.moderngas.repository.CartRepo;
import com.moderngas.repository.GasRepo;
import com.moderngas.repository.OrderRepo;
import com.moderngas.repository.UserRepo;
import com.moderngas.service.GenericService;
import com.moderngas.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
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
    public List<OrderDto> getOrderListByUser(Long userId) {
        UserEntity user = userRepo.findById(userId).orElse(null);
        if (null == user) {
            throw new BadRequestException(ExceptionConstants.INVALID_USER);
        }
        List<OrderDto> orderDtoList = new ArrayList<>();
        List<OrderEntity> orderEntityList = orderRepo.getOrderEntitiesByUserId(userId);
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
    public List<CartDto> getCartByUser(Long userId) {
        UserEntity user = userRepo.findById(userId).orElse(null);
        if (null == user) {
            throw new BadRequestException(ExceptionConstants.INVALID_USER);
        }
        List<CartDto> cartDtoList = new ArrayList<>();
        List<CartEntity> cartEntityList = cartRepo.getCartEntitiesByUserIdOrderByUpdatedDate(userId);
        if (!CollectionUtils.isEmpty(cartEntityList)) {
            cartDtoList = cartEntityList.stream()
                    .map(e -> genericService.convertCartEntityToDto(e)).collect(Collectors.toList());
        }
        return cartDtoList;
    }

    @Override
    public String deleteOrder(Long orderId) {
        log.info("OrderService >> Delete Order {}", orderId);
        OrderEntity order = orderRepo.findById(orderId).orElse(null);
        if (null == order) {
            throw new BadRequestException(ExceptionConstants.INVALID_ORDER);
        }
        orderRepo.deleteOrderById(orderId);
        return Constants.SUCCESS_STR;
    }

    @Override
    public String deleteCart(Long cartId) {
        log.info("OrderService >> Delete Cart {}", cartId);
        CartEntity cart = cartRepo.findById(cartId).orElse(null);
        if (null == cart) {
            throw new BadRequestException(ExceptionConstants.INVALID_CART);
        }
        cartRepo.deleteById(cartId);
        return Constants.SUCCESS_STR;
    }

    @Override
    public String placeOrderFromCart(Long userId) {
        log.info("OrderService >> Place Order From Cart By User: {}", userId);
        UserEntity user = userRepo.findById(userId).orElse(null);
        if (null == user) {
            throw new BadRequestException(ExceptionConstants.INVALID_USER);
        }
        String response = Constants.FAILURE_STR;
        List<CartEntity> cartEntityList = cartRepo.getCartEntitiesByUserIdOrderByUpdatedDate(userId);
        if (!CollectionUtils.isEmpty(cartEntityList)) {
            List<OrderEntity> orderEntityList = genericService.convertCartToOrderEntity(cartEntityList);
            orderRepo.saveAll(orderEntityList);
            cartRepo.deleteByUserId(userId);
            response = Constants.SUCCESS_STR;
        }
        return response;
    }

    @Override
    public OrderDto getOrderDetailsById(Long orderId) {
        OrderEntity order = orderRepo.findById(orderId).orElse(null);
        if (null == order) {
            throw new BadRequestException(ExceptionConstants.INVALID_ORDER);
        }
        OrderEntity orderEntity = orderRepo.getOrderEntitiesById(orderId);
        UserEntity userEntity = userRepo.findById(orderEntity.getUserId()).orElse(null);
        OrderDto orderDto = genericService.convertOrderEntityToDto(orderEntity);
        orderDto.setAddressDto(genericService.convertAddressEntityToDto(userEntity.getAddressEntity()));
        orderDto.setUserName(userEntity.getName());
        orderDto.setOrderedOnDate(orderEntity.getCreatedDate());
        orderDto.setLoadedOnDate(orderEntity.getLoadedDate());
        orderDto.setDeliveredOnDate(orderEntity.getDeliveredDate());
        return orderDto;
    }

    @Override
    public String updateOrderStatus(Long orderId, Long statusId) throws Exception {
        log.info("OrderService >>");
        OrderEntity order = orderRepo.findById(orderId).orElse(null);
        if (null == order) {
            throw new BadRequestException(ExceptionConstants.INVALID_ORDER);
        }
        OrderStatus orderStatus = OrderStatus.getByOrdinal(statusId.intValue());
        if (null == orderStatus) {
            throw new BadRequestException(ExceptionConstants.INVALID_STATUS);
        }
        String response = Constants.FAILURE_STR;
        OrderEntity orderEntity = orderRepo.getOrderEntitiesById(orderId);
        if (null != orderEntity) {
            orderEntity = genericService.changeOrderStatus(orderEntity, orderStatus);
            orderRepo.save(orderEntity);
            response = Constants.SUCCESS_STR;
        }
        return response;
    }

    @Override
    public Page<com.moderngas.pojo.admin.OrderDto> getAllOrderListForAdmin(Pageable pageable, String status, String cylinderType, String search, String quantityOrder) {
        OrderStatus statusEnum = OrderStatus.getByStatus(status);
        CylinderType cylinderTypeEnum = CylinderType.getByStatus(cylinderType);
        return orderRepo.getAllOrderListForAdmin(pageable, statusEnum, cylinderTypeEnum, search, quantityOrder);
    }
}