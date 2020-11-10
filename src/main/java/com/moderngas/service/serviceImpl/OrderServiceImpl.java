package com.moderngas.service.serviceImpl;

import com.moderngas.jpaentity.CartEntity;
import com.moderngas.jpaentity.OrderEntity;
import com.moderngas.pojo.CartDto;
import com.moderngas.pojo.OrderDto;
import com.moderngas.repository.CartRepo;
import com.moderngas.repository.OrderRepo;
import com.moderngas.service.GenericService;
import com.moderngas.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    GenericService genericService;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    CartRepo cartRepo;

    @Override
    public String placeOrder(OrderDto orderDto) {
        String response = "Failure";
        OrderEntity orderEntity = genericService.convertDtoToOrderEntity(orderDto);
        if (null != orderEntity) {
            orderRepo.save(orderEntity);
            response = "Success";
        }
        return response;
    }

    @Override
    public List<OrderDto> getOrderListByUser(Long userId) {
        List<OrderDto> orderDtoList = new ArrayList<>();
        List<OrderEntity> orderEntityList = orderRepo.getOrderEntitiesByUserIdAndActiveFlagTrueOrderByUpdatedDate(userId);
        if (!CollectionUtils.isEmpty(orderEntityList)) {
            orderDtoList = orderEntityList.stream()
                    .map(e -> genericService.convertOrderEntityToDto(e)).collect(Collectors.toList());
        }
        return orderDtoList;
    }

    @Override
    public String addCart(CartDto cartDto) {
        String response = "Failure";
        CartEntity cartEntity = genericService.convertDtoToCartEntity(cartDto);
        if (null != cartEntity) {
            cartRepo.save(cartEntity);
            response = "Success";
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
        String response = "Success";
        orderRepo.deleteOrderById(orderId);
        return response;
    }

    @Override
    public String deleteCart(Long cartId) {
        String response = "Success";
        cartRepo.deleteById(cartId);
        return response;
    }

    @Override
    public String placeOrderFromCart(Long userId) {
        String response = "Failure";
        List<CartEntity> cartEntityList = cartRepo.getCartEntitiesByUserIdOrderByUpdatedDate(userId);
        if (!CollectionUtils.isEmpty(cartEntityList)) {
            List<OrderEntity> orderEntityList = genericService.convertCartToOrderEntity(cartEntityList);
            orderRepo.saveAll(orderEntityList);

            /* Delete Cart Entity */
            cartRepo.deleteByUserId(userId);
            response = "Success";
        }
        return response;
    }
}