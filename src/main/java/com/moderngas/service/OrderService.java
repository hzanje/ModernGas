package com.moderngas.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.moderngas.pojo.user.CartDto;
import com.moderngas.pojo.NameIdDto;
import com.moderngas.pojo.user.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

    String placeOrder(OrderDto orderDto);

    List<OrderDto> getOrderListByUser(Long userId);

    String addCart(CartDto cartDto);

    List<CartDto> getCartByUser(Long userId);

    String deleteOrder(Long orderId);

    String deleteCart(Long cartId);

    String placeOrderFromCart(Long userId);

    OrderDto getOrderDetailsById(Long orderId);

    String updateOrderStatus(Long orderId, Long statusId) throws Exception;

    Page<com.moderngas.pojo.admin.OrderDto> getAllOrderListForAdmin(Pageable pageable, String status, List<String> cylinderType, String search, String quantityOrder) throws JsonProcessingException;
}
