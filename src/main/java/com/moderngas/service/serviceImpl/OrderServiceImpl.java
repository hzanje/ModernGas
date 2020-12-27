package com.moderngas.service.serviceImpl;

import com.moderngas.jpaentity.CartEntity;
import com.moderngas.jpaentity.OrderEntity;
import com.moderngas.jpaentity.UserEntity;
import com.moderngas.pojo.CartDto;
import com.moderngas.pojo.NameIdDto;
import com.moderngas.pojo.OrderDto;
import com.moderngas.repository.CartRepo;
import com.moderngas.repository.GasRepo;
import com.moderngas.repository.OrderRepo;
import com.moderngas.repository.UserRepo;
import com.moderngas.service.GenericService;
import com.moderngas.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    GenericService genericService;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    CartRepo cartRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    GasRepo gasRepo;

    private static final String FAILURE_STR = "Failure";

    private static final String SUCCESS_STR = "Success";

    @Override
    public String placeOrder(OrderDto orderDto) {
        String response = FAILURE_STR;
        OrderEntity orderEntity = genericService.convertDtoToOrderEntity(orderDto);
        if (null != orderEntity) {
            orderRepo.save(orderEntity);
            response = SUCCESS_STR;
        }
        return response;
    }

    @Override
    public List<OrderDto> getOrderListByUser(Long userId) {
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
        String response = FAILURE_STR;
        CartEntity cartEntity = genericService.convertDtoToCartEntity(cartDto);
        if (null != cartEntity) {
            cartRepo.save(cartEntity);
            response = SUCCESS_STR;
        }
        return response;
    }

    @Override
    public List<CartDto> getCartByUser(Long userId) {
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
        String response = FAILURE_STR;
        orderRepo.deleteOrderById(orderId);
        return response;
    }

    @Override
    public String deleteCart(Long cartId) {
        String response = SUCCESS_STR;
        cartRepo.deleteById(cartId);
        return response;
    }

    @Override
    public String placeOrderFromCart(Long userId) {
        String response = FAILURE_STR;
        List<CartEntity> cartEntityList = cartRepo.getCartEntitiesByUserIdOrderByUpdatedDate(userId);
        if (!CollectionUtils.isEmpty(cartEntityList)) {
            List<OrderEntity> orderEntityList = genericService.convertCartToOrderEntity(cartEntityList);
            orderRepo.saveAll(orderEntityList);

            /* Delete Cart Entity */
            cartRepo.deleteByUserId(userId);
            response = SUCCESS_STR;
        }
        return response;
    }

    @Override
    public OrderDto getOrderDetailsById(Long orderId) {
        OrderEntity orderEntity = orderRepo.getOrderEntitiesById(orderId);
        UserEntity userEntity = userRepo.getOne(orderEntity.getUserId());
        OrderDto orderDto = genericService.convertOrderEntityToDto(orderEntity);
        orderDto.setAddressDto(genericService.convertAddressEntityToDto(userEntity.getAddressEntity()));
        orderDto.setOrderedOnDate(orderEntity.getOrderDate());
        orderDto.setLoadedOnDate(orderEntity.getLoadedDate());
        orderDto.setShippedOnDate(orderEntity.getDispatchedDate());
        orderDto.setDeliveredOnDate(orderEntity.getDeliveredDate());
        return orderDto;
    }

    @Override
    public String updateOrderStatus(Long orderId, Long statusId) {
        String response = FAILURE_STR;
        OrderEntity orderEntity = orderRepo.getOrderEntitiesById(orderId);
        if (null != orderEntity) {
            orderEntity = genericService.changeOrderStatus(orderEntity, statusId);
            orderRepo.save(orderEntity);
            response = SUCCESS_STR;
        }
        return response;
    }

    @Override
    public List<NameIdDto> getOrderStatusList() {
        return gasRepo.getAllStatus().stream().map(s -> {
            NameIdDto nameIdDto = new NameIdDto();
            nameIdDto.setId(s.getId());
            nameIdDto.setName(s.getStatus());
            return nameIdDto;
        }).collect(Collectors.toList());
    }
}