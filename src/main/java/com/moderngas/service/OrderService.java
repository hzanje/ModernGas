package com.moderngas.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.admin.OpenOrderDto;
import com.moderngas.pojo.user.CartDto;
import com.moderngas.pojo.user.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

    String placeOrder(OrderDto orderDto) throws BadRequestException;

    List<OrderDto> getOrderListByUser(Long userId, Long adminId) throws BadRequestException;

    String addOrUpdateCart(CartDto cartDto) throws BadRequestException;

    List<CartDto> getCartByUser(Long userId, Long adminId) throws BadRequestException;

    String deleteOrder(Long orderId) throws BadRequestException;

    String deleteCart(Long cartId) throws BadRequestException;

    String placeOrderFromCart(Long userId, Long adminId, Long addressId) throws BadRequestException;

    OrderDto getOrderDetailsById(Long orderId) throws BadRequestException;

    String updateOrderStatus(Long orderId, String orderStatus, Long vehicleNumber) throws BadRequestException;

    Page<com.moderngas.pojo.admin.OrderDto> getAllOrderListForAdmin(Pageable pageable, String status, List<String> cylinderType, String search, Long id, String quantityOrder) throws JsonProcessingException, BadRequestException;

    List<com.moderngas.pojo.admin.OrderDto> getUserOrderListForAdminInUserDetails(Long userId) throws BadRequestException;

    String placeAdminInitiatedOrder(OpenOrderDto openOrderDto, Long adminId) throws BadRequestException;
}
